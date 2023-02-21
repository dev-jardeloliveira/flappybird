package com.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

import javax.xml.crypto.dsig.CanonicalizationMethod;

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
	private int pontuacaoMaxima = 0;
	private boolean passouCano;
	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
    private int estadoJogo = 0;

	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;
	Preferences preferencias;
	private OrthographicCamera camera;
	private Viewport viewport;
	private float VIRTUAL_WIDTH;
	private float VIRTUAL_HEIGHT;

	@Override
	public void create () {
		//Gdx.app.log("create","jogo iniciado");
		initTexturas();
		initObjetos();
	}

	@Override
	public void render () {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
          if(estadoJogo == 1 ){

			  somColisao.play();
			  estadoJogo = 2;
		  }

		}
	}

	private void verificarEstados(){
        boolean toqueTela = Gdx.input.justTouched();
        if(estadoJogo == 0){

            if(toqueTela){
                gravidade = -12;
                estadoJogo = 1;
				somVoando.play();
            }


        } else if (estadoJogo == 1) {
			if(toqueTela){
				gravidade = -12;
			}
            posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
            if(posicaoCanoHorizontal < - canoBaixo.getWidth()){
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVerticalTopo= random.nextInt(180)-40;
                posicaoCanoVertical = random.nextInt(110)-60;
                passouCano = false;
            }
            if(posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

            gravidade++;
			somVoando.play();


        } else if (estadoJogo == 2) {
			if(pontos > pontuacaoMaxima){

				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);

			}
				if(toqueTela){
					estadoJogo = 0;
					pontos = 0 ;
					gravidade = 0 ;
					posicaoCanoVertical = alturaDispositivo /2;
					posicaoCanoHorizontal = larguraDispositivo;

				}
        }




	}
	private void desenharTexturas(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],50,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo,posicaoCanoHorizontal,(alturaDispositivo/2)-canoBaixo.getHeight() - (espacoEntreCanos + posicaoCanoVerticalTopo));
		batch.draw(canoTopo,posicaoCanoHorizontal,(alturaDispositivo/2)+(espacoEntreCanos + posicaoCanoVertical));
		textoPontuacao.draw(batch, String.valueOf(pontos),larguraDispositivo/2, alturaDispositivo-100);
		if(estadoJogo == 2){
			batch.draw(gameOver, (larguraDispositivo/2) - (gameOver.getWidth()/2) , alturaDispositivo /2);
			textoReiniciar.draw(batch,"Toque para reiniciar!", larguraDispositivo/2 -290 , alturaDispositivo/2 - (gameOver.getHeight()/2));
			textoMelhorPontuacao.draw(batch,"Seu record é: "+ pontuacaoMaxima +" pontos",larguraDispositivo/2 -290 ,alturaDispositivo/2 -gameOver.getHeight() -40);
		}
		batch.end();
	}

	public void validarPontos(){
		if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){
			if(!passouCano){
				pontos ++;
				passouCano = true;
				somPontuacao.play();
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
		VIRTUAL_WIDTH = Gdx.graphics.getWidth();
		VIRTUAL_HEIGHT = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		random = new Random();
		larguraDispositivo  = Gdx.graphics.getWidth() ;
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo /2;
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 210;

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

		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));

		preferencias = Gdx.app.getPreferences("flappyBird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);

		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2,0);
		viewport = new StretchViewport (VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);


	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
	}

	@Override
	public void dispose () {

	}
}
