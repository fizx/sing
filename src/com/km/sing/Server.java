package com.km.sing;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import com.martiansoftware.nailgun.NGContext;

public class Server {
	public static Server active = null;

	private String root;
	private Compiler compiler;
	private FileFilter dirFilter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};
	private FileFilter scalaFilter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.getPath().endsWith(".scala");
		}
	};
	private int watchId;

	public Server(String path) {
		root = path;
		compiler = new Compiler();
	}

	public void start() throws JNotifyException {
		List<File> list = scalaFilesIn(new File(root));
		compiler.compile(list);
		watchId = JNotify.addWatch(root, JNotify.FILE_CREATED
				| JNotify.FILE_MODIFIED, true, new Listener(compiler));
	}

	public void stop() throws JNotifyException {
		JNotify.removeWatch(watchId);
	}

	private List<File> scalaFilesIn(File file) {
		ArrayList<File> list = new ArrayList<File>();
		list.addAll(Arrays.asList(file.listFiles(scalaFilter)));
		for (File dir : file.listFiles(dirFilter)) {
			list.addAll(scalaFilesIn(dir));
		}
		return list;
	}

	public static void nailMain(NGContext context) {
		String[] args = context.getArgs();
		if (args.length == 0) {
			System.err.println("Please specify a subcommand: ");
			System.err.println("  compile FILE_NAME");
			System.err.println("  spec    CLASS_NAME");
			System.err.println("  specall");
			System.exit(1);
		}
		String command = args[0];
		if (command.equals("compile")) {
			active.compiler.compile(new File(args[1]));
		} else {
			System.err.println("Subcommand not recognized.");
			System.exit(1);
		}
		System.out.println();
	}

	public static void main(String args[]) throws Exception {
		if (args.length < 1) {
			System.err.println("Please provide a path to watch!");
			System.exit(1);
		}
		Server server = new Server(args[0]);
		server.start();
		Server.active = server;
		com.martiansoftware.nailgun.NGServer.main(new String[0]);
	}
}
