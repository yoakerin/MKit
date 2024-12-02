package com.yoake.video_player.util.render;

import android.content.Context;

import xyz.doikki.videoplayer.render.IRenderView;
import xyz.doikki.videoplayer.render.RenderViewFactory;

public class GLTextureRenderViewFactory extends RenderViewFactory {

    public static GLTextureRenderViewFactory create() {
        return new GLTextureRenderViewFactory();
    }

    @Override
    public IRenderView createRenderView(Context context) {
        return new GLTextureRenderView(context);
    }
}
