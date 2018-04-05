package test;

import game.Screen;

public class MockScreen implements Screen {
	private int height;
	private int width;

	public MockScreen(int height, int width) {
		this.height = height;
		this.width = width;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
