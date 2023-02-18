package com.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Game extends ApplicationAdapter {
		private float variacao = 0;
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVerticalTopo;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private Random random;
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	private BitmapFont textoPontuacao;
	private BitmapFont textoReiniciar;
	private BitmapFont textoMelhorPontuacao;
	private int pontos = 0;
	private boolean passouCano;
	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
    private int estadoJogo = 0;
	@Override
	public void create () {
		//Gdx.app.log("create","jogo iniciado");
		initTexturas();
		initObjetos();
	}

	@Override
	public void render () {

		verificarEstados();
		validarPontos();
		desenharTexturas();
		detectarColisoes();
	}

	private void detectarColisoes() {
		circuloPassaro.set(50+passaros[0].getWidth()/2, posicaoInicialVerticalPassaro + passaros[0].getHeight()/2, passaros[0].getWidth()/2);
		retanguloCanoTopo.set(posicaoCanoHorizontal,(alturaDispositivo/2)+(espacoEntreCanos + posicaoCanoVertical), canoTopo.getWidth(),canoTopo.getHeight());
		retanguloCanoBaixo.set(posicaoCanoHorizontal,(alturaDispositivo/2)-canoBaixo.getHeight() - (espacoEntreCanos + posicaoCanoVerticalTopo),canoBaixo.getWidth(),canoBaixo.getHeight());
        if(Intersector.overlaps(circuloPassaro,retanguloCanoTopo) || Intersector.overlaps(circuloPassaro,retanguloCanoBaixo)){
           estadoJogo = 2;
		}
	}

	private void verificarEstados(){
        boolean toqueTela = Gdx.input.justTouched();
        if(estadoJogo == 0){

            if(toqueTela){
                gravidade = -15;
                estadoJogo = 1;
            }


        } else if (estadoJogo == 1) {
			if(toqueTela){
				gravidade = -15;
			}
            posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
            if(posicaoCanoHorizontal < - canoBaixo.getWidth()){
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVerticalTopo= random.nextInt(800)-400;
                posicaoCanoVertical = random.nextInt(800)-100;
                passouCano = false;
            }
            if(posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;


            gravidade++;


        } else if (estadoJogo == 2) {

        }




	}
	private void desenharTexturas(){
		batch.begin();
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],50,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo,posicaoCanoHorizontal,(alturaDispositivo/2)-canoBaixo.getHeight() - (espacoEntreCanos + posicaoCanoVerticalTopo));
		batch.draw(canoTopo,posicaoCanoHorizontal,(alturaDispositivo/2)+(espacoEntreCanos + posicaoCanoVertical));
		textoPontuacao.draw(batch, String.valueOf(pontos),larguraDispositivo/2, alturaDispositivo-100);
		if(estadoJogo == 2){
			batch.draw(gameOver, (larguraDispositivo/2) - (gameOver.getWidth()/2) , alturaDispositivo /2);
			textoReiniciar.draw(batch,"Toque para reiniciar!", larguraDispositivo/2 -140, alturaDispositivo/2 - (gameOver.getHeight()/2));
			textoMelhorPontuacao.draw(batch,"Seu record é: 0 pontos",larguraDispositivo/2 -140,alturaDispositivo/2 -gameOver.getHeight());
		}
		batch.end();
	}

	public void validarPontos(){
		if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){
			if(!passouCano){
				pontos ++;
				passouCano = true;
			}

		}
        variacao+= Gdx.graphics.getDeltaTime() * 10;

        if(variacao > 3)
            variacao=0;
	}
	private void initTexturas(){
		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
		gameOver = new Texture("game_over.png");
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

		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);

		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.GREEN);
		textoReiniciar.getData().setScale(4);

		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.RED);
		textoMelhorPontuacao.getData().setScale(4);

		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoTopo = new Rectangle();
		retanguloCanoBaixo = new Rectangle();
	}
	@Override
	public void dispose () {

	}
}
