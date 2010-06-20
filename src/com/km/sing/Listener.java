package com.km.sing;

import java.io.File;

import net.contentobjects.jnotify.JNotifyListener;

public class Listener implements JNotifyListener {

	private Compiler compiler;

	public Listener(Compiler compiler) {
		this.compiler = compiler;
	}

	@Override
	public void fileCreated(int wd, String root, String name) {
		compiler.compile(new File(root, name));
	}

	@Override
	public void fileDeleted(int wd, String root, String name) {

	}

	@Override
	public void fileModified(int wd, String root, String name) {
		compiler.compile(new File(root, name));
	}

	@Override
	public void fileRenamed(int wd, String root, String name, String newName) {
		compiler.compile(new File(root, newName));
	}

}
