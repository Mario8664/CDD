package com.saveandstudio.mario.cdd.Renderers;

import android.graphics.*;
import android.os.Debug;
import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;

public class CardRenderer extends Renderer {

    private Transform transform;
    private Bitmap suit, background, cardBack, figure;
    private int suitID, backgroundID, cardBackID, figureID;
    private boolean side = false;
    private int bitMapHeight, bitMapWidth;
    private boolean set;


    public CardRenderer() {
        this(R.mipmap.diamond, R.mipmap.card_background, R.mipmap.card_back, R.mipmap.diamond);
    }

    public CardRenderer(int suit, int figure) {
        this(suit, R.mipmap.card_background, R.mipmap.card_back, figure);
    }

    public CardRenderer(int suit, int background, int cardBack, int figure) {
        set = true;
        suitID = suit;
        backgroundID = background;
        cardBackID = cardBack;
        figureID = figure;
    }

    public void setSide(boolean side) {
        this.side = side;
    }

    public void setCardID(int suitID, int figureID) {
        this.suitID = suitID;
        this.figureID = figureID;
        set = true;
    }

    @Override
    public void Draw(Canvas canvas, Paint paint) {
        if (transform == null) {
            transform = (Transform) getComponent(Transform.class);
        } else {
            if (Global.surfaceContext != null) {
                if (suit == null) {
                    suit = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), suitID);
                }
                if (background == null) {
                    background = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), backgroundID);
                    bitMapWidth = background.getWidth();
                    bitMapHeight = background.getHeight();
                }
                if (cardBack == null) {
                    cardBack = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), cardBackID);
                }
                if (figure == null) {
                    figure = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), figureID);
                }
            }
        }
        if (set) {
            if (Global.surfaceContext != null) {
                suit = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), suitID);
                background = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), backgroundID);
                bitMapWidth = background.getWidth();
                bitMapHeight = background.getHeight();
                cardBack = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), cardBackID);
                figure = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), figureID);
            }
            set = false;

        }
        if (suit != null && background != null && cardBack != null) {
            Vector3 pivot = transform.getPivot();
            Vector3 position = transform.getPosition();
            Vector3 scale = transform.getScale();
            if (side) {
                transform.
                        setPivot(new Vector3((float) background.getWidth() / 2, (float) background.getHeight() / 2, 0));
                canvas.drawBitmap(background, transform.transformMatrix, null);
                transform.setPivot(new Vector3((float) suit.getWidth() / 2, (float) suit.getHeight() / 2, 0));
                canvas.drawBitmap(suit, transform.transformMatrix, null);
                //Draw figure
                Vector3 distance = new Vector3(bitMapWidth, bitMapHeight, 0).divide(new Vector3(3, 5, 1)).
                        multiply(new Vector3((float) 2.8, (float) 2.8, 1));
                transform.setPivot(transform.getPivot().add(distance));
                transform.setScale(scale.divide(new Vector3((float) 2.8, (float) 2.8, 1)));
                canvas.drawBitmap(suit, transform.transformMatrix, null);
                distance = new Vector3(0, bitMapHeight, 0).divide(new Vector3(1, (float) 6, 1)).
                        multiply(new Vector3((float) 2.8, (float) 2.8, 1));
                transform.setPivot(transform.getPivot().add(distance));
                canvas.drawBitmap(figure, transform.transformMatrix, null);

            } else {
                transform.setPivot(new Vector3((float) cardBack.getWidth() / 2, (float) cardBack.getHeight() / 2, 0));
                canvas.drawBitmap(cardBack, transform.transformMatrix, null);
            }
            transform.setPivot(pivot);
            transform.setPosition(position);
            transform.setScale(scale);

        }
        zDepth = transform.getPosition().z;
        if (gameObject.toBeDestroy) {
            Destroy();
        }
    }

    public int getBitMapWidth() {
        return bitMapWidth;
    }

    public int getBitMapHeight() {
        return bitMapHeight;
    }


}
