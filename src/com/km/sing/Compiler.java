package com.km.sing;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import scala.collection.jcl.Conversions;
import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;
import scala.tools.nsc.io.PlainFile;
import scala.tools.nsc.util.BatchSourceFile;
import scala.tools.nsc.util.SourceFile;

@SuppressWarnings("serial")
public class Compiler {

	private final Interpreter interpreter;

	public Compiler() {
		interpreter = new Interpreter(new Settings());
	}

	public void compile(final File file) {
		compile(new LinkedList<File>() {
			{
				add(file);
			}
		});
	}

	public void compile(final List<File> files) {
		if (interpreter.compileSources(convert(files))) {
			System.out.println("OK");
		}
	}

	@SuppressWarnings("unchecked")
	private scala.List<SourceFile> convert(List<File> files) {
		ArrayList<SourceFile> sourceFiles = new ArrayList<SourceFile>();
		for (File file : files) {
			sourceFiles.add(new BatchSourceFile(new PlainFile(file)));
		}
		return (scala.List<SourceFile>) Conversions.convertList(sourceFiles);
	}

}
