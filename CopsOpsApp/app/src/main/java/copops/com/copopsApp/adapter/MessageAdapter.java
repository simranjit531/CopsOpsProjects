package copops.com.copopsApp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.Callback;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import copops.com.copopsApp.R;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.utils.ChatHolder;
import copops.com.copopsApp.utils.Utils;
import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;

import static android.media.ThumbnailUtils.createVideoThumbnail;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;

    private ArrayList<ChatHolder> mChatHolders;


    String name;

    public MessageAdapter(Context context, ArrayList<ChatHolder> mChatHolders, String name) {
        this.context = context;
        this.mChatHolders = mChatHolders;
        this.name = name;

    }
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View convertView;

        convertView = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);

        return new MessageAdapter.ViewHolder(convertView);
    }


    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder viewHolder, final int i) {

        if (mChatHolders.get(i).getMessage_type().equals("TEXT")) {
            if (mChatHolders.get(i).getSender().equalsIgnoreCase("ME")) {
                viewHolder.textId.setText(mChatHolders.get(i).getMessage());
                viewHolder.nameTv.setText(context.getString(R.string.me));
                viewHolder.nameTv.setTextColor(context.getResources().getColor(R.color.blue));
                viewHolder.meViewId.setVisibility(View.VISIBLE);
                viewHolder.otherViewId.setVisibility(View.GONE);
            } else {
                viewHolder.textId.setText(mChatHolders.get(i).getMessage());
                viewHolder.nameTv.setText(mChatHolders.get(i).getSender());
                viewHolder.otherViewId.setVisibility(View.VISIBLE);
                viewHolder.nameTv.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                viewHolder.meViewId.setVisibility(View.GONE);
            }
        } else if (mChatHolders.get(i).getMessage_type().equals("IMAGE")) {
            viewHolder.set_image_Viewid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater factory = LayoutInflater.from(context);
                    final View deleteDialogView = factory.inflate(R.layout.preview_xml, null);
                    final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                    ImageView image = (ImageView) deleteDialogView.findViewById(R.id.PreviewImageViewid);
                    ImageView cross = (ImageView) deleteDialogView.findViewById(R.id.cross);
                    CircularProgressDrawable circularProgressDrawable = new  CircularProgressDrawable(context);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();

                    Glide.with(context)
                            .load(mChatHolders.get(i).getMessage())
                            .placeholder(circularProgressDrawable)
                            .into(image);
                    deleteDialog.setView(deleteDialogView);

                    cross.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //your business logic
                            deleteDialog.dismiss();
                        }
                    });
                    deleteDialog.show();
                }
            });
            if (mChatHolders.get(i).getSender().equalsIgnoreCase("ME")) {
                CircularProgressDrawable circularProgressDrawable = new  CircularProgressDrawable(context);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();

//                Glide.with(context)
//                        .load(mChatHolders.get(i).getThumb())
//                        .placeholder(circularProgressDrawable)
//                        .into(viewHolder.set_image_Viewid);
                viewHolder.ImagenameTv.setText(context.getString(R.string.me));
                viewHolder.ImagenameTv.setTextColor(context.getResources().getColor(R.color.blue));
                viewHolder.ImagemeViewId.setVisibility(View.VISIBLE);
                viewHolder.ImageotherViewId.setVisibility(View.GONE);
            } else {
                CircularProgressDrawable circularProgressDrawable = new  CircularProgressDrawable(context);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();
//                Glide.with(context)
//                        .load(mChatHolders.get(i).getThumb())
//                        .placeholder(circularProgressDrawable)
//                        .into(viewHolder.set_image_Viewid);
                viewHolder.ImagenameTv.setText(mChatHolders.get(i).getSender());
                viewHolder.ImageotherViewId.setVisibility(View.VISIBLE);
                viewHolder.ImagenameTv.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                viewHolder.ImagemeViewId.setVisibility(View.GONE);
            }
        } else if (mChatHolders.get(i).getMessage_type().equals("VIDEO")) {

            viewHolder.videoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View deleteDialogView = factory.inflate(R.layout.videoplau_item_preview, null);
                    final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                    @SuppressLint("WrongViewCast")
                    FullscreenVideoLayout imageVideo = (FullscreenVideoLayout) deleteDialogView.findViewById(R.id.mpw_video_player);
                    ImageView crossVideo = (ImageView) deleteDialogView.findViewById(R.id.crossVideo);



                    imageVideo.setActivity((DashboardActivity) context);


                    Uri videoUri = Uri.parse(mChatHolders.get(i).getMessage());
                    try {
                        imageVideo.setVideoURI(videoUri);
                        imageVideo.setShouldAutoplay(true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    deleteDialog.setView(deleteDialogView);
                    crossVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //your business logic
                            deleteDialog.dismiss();
                            imageVideo.setShouldAutoplay(false);
                        }
                    });
                    deleteDialog.show();
                }
            });
            if (mChatHolders.get(i).getSender().equalsIgnoreCase("ME")) {

                viewHolder.ImagenameTv.setText(context.getString(R.string.me));
                viewHolder.ImagenameTv.setTextColor(context.getResources().getColor(R.color.blue));
                viewHolder.ImagemeViewId.setVisibility(View.VISIBLE);
                viewHolder.ImageotherViewId.setVisibility(View.GONE);
            } else {

                viewHolder.ImagenameTv.setText(mChatHolders.get(i).getSender());
                viewHolder.ImageotherViewId.setVisibility(View.VISIBLE);
                viewHolder.ImagenameTv.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                viewHolder.ImagemeViewId.setVisibility(View.GONE);
            }
        } else {
            viewHolder.webviewpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      WebView webView= new WebView(context);
                    String pdf = mChatHolders.get(i).getMessage();
                    webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

                }
            });


            if (mChatHolders.get(i).getSender().equalsIgnoreCase("ME")) {

                String filenameset=mChatHolders.get(i).getMessage().substring(mChatHolders.get(i).getMessage().lastIndexOf("/")+1);
                //  File file = new File(mChatHolders.get(i).getMessage());
                viewHolder.filename.setText(filenameset);
                viewHolder.ImagenameTv.setText(context.getString(R.string.me));
                viewHolder.ImagenameTv.setTextColor(context.getResources().getColor(R.color.blue));
                viewHolder.ImagemeViewId.setVisibility(View.VISIBLE);
                viewHolder.ImageotherViewId.setVisibility(View.GONE);

            } else {
                String filenameset=mChatHolders.get(i).getMessage().substring(mChatHolders.get(i).getMessage().lastIndexOf("/")+1);
                viewHolder.filename.setText(filenameset);
                viewHolder.ImagenameTv.setText(mChatHolders.get(i).getSender());
                viewHolder.ImageotherViewId.setVisibility(View.VISIBLE);
                viewHolder.ImagenameTv.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                viewHolder.ImagemeViewId.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mChatHolders.size();
    }

    @Override
    public int getItemViewType(final int position) {

        if (mChatHolders.get(position).getMessage_type().equals("TEXT")) {
            return R.layout.chat_item;
        } else if (mChatHolders.get(position).getMessage_type().equals("IMAGE")) {
            return R.layout.image_view_item;
        } else if (mChatHolders.get(position).getMessage_type().equals("PDF")) {

            return R.layout.pdf_view_item;
        } else {
            return R.layout.video_view;
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textId, nameTv, ImagenameTv,filename;
        View meViewId, otherViewId, ImagemeViewId, ImageotherViewId;


        ImageView set_image_Viewid;
        ImageView videoLayout;
        ImageView webviewpdf;

        public ViewHolder(View view) {
            super(view);

            textId = (TextView) view.findViewById(R.id.textId);
            ImagenameTv = (TextView) view.findViewById(R.id.ImagenameTv);

            nameTv = (TextView) view.findViewById(R.id.nameTv);
            meViewId = (View) view.findViewById(R.id.meViewId);
            ImagemeViewId = (View) view.findViewById(R.id.ImagemeViewId);
            otherViewId = (View) view.findViewById(R.id.otherViewId);
            ImageotherViewId = (View) view.findViewById(R.id.ImageotherViewId);
            set_image_Viewid = (ImageView) view.findViewById(R.id.image_Viewid);
            videoLayout = (ImageView) view.findViewById(R.id.videoView);
            webviewpdf = (ImageView) view.findViewById(R.id.clickPdf);
            filename = (TextView) view.findViewById(R.id.filename);

        }



    }

}