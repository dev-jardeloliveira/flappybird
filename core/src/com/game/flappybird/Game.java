package com.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Game extends ApplicationAdapter {
		private float variacao = 0;
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private Random random;
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	@Override
	public void create () {
		//Gdx.app.log("create","jogo iniciado");
		initTexturas();
		initObjetos();
	}

	@Override
	public void render () {

		verificarEstados();
		desenharTexturas();
	}

	private void verificarEstados(){

		posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
		if(posicaoCanoHorizontal < - canoBaixo.getWidth()){
			posicaoCanoHorizontal = larguraDispositivo;
			posicaoCanoVertical = random.nextInt(800)-400;
		}
		boolean toqueTela = Gdx.input.justTouched();
		if(toqueTela){
			gravidade = -15;
		}

		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;


		gravidade++;
		variacao+= Gdx.graphics.getDeltaTime() * 10;

		if(variacao > 3)
			variacao=0;

	}
	private void desenharTexturas(){
		batch.begin();
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],30,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo,posicaoCanoHorizontal,(alturaDispositivo/2)-canoBaixo.getHeight() - (espacoEntreCanos + posicaoCanoVertical));
		batch.draw(canoTopo,posicaoCanoHorizontal,(alturaDispositivo/2)+(espacoEntreCanos + posicaoCanoVertical));
		batch.end();
	}
	private void initTexturas(){
		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
		passaros=new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
	}
	private void initObjetos(){
		batch = new SpriteBatch();
		random = new Random();
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo /2;
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 250;
	}
	@Override
	public void dispose () {

	}
}
