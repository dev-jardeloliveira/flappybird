package com.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	private int movimentoX = 0;
	private float variacao = 0;
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	@Override
	public void create () {
		//Gdx.app.log("create","jogo iniciado");
		batch = new SpriteBatch();
		fundo = new Texture("fundo.png");
		passaros=new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo /2;
	}

	@Override
	public void render () {
        batch.begin();
		if(variacao > 3)
			variacao=0;
		boolean toqueTela = Gdx.input.justTouched();
		if(toqueTela){
			gravidade = -20;
		}


		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
		posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],30,posicaoInicialVerticalPassaro);
		movimentoX++;
		gravidade++;
		variacao+= Gdx.graphics.getDeltaTime() * 10;
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
