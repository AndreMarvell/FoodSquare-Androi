package com.andremarvell.foodsquare.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.adapter.GalerieGridAdapter;
import com.andremarvell.foodsquare.adapter.RestaurantCommentAdapter;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.webservices.FavorisService;
import com.andremarvell.foodsquare.webservices.ratecomment.CommentService;
import com.andremarvell.foodsquare.webservices.ratecomment.RateService;
import com.andremarvell.foodsquare.webservices.restaurant.PhotoUploader;
import com.andremarvell.foodsquare.webservices.restaurant.RestaurantService;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RestaurantFragment extends SherlockFragment {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String ARG_RESTO = "restaurant";

    View _rootView;
    ListView listViewCommentaire;
    RestaurantCommentAdapter adapter;
    Restaurant restaurant;
    String[] typeName = {"À Propos","Avis", "Galerie"};
    TabHost tabHost;

    TextView nomResto;
    TextView numNoteResto;
    TextView numCommentResto;
    TextView userNoteResto;
    TextView noteResto;
    TextView adresseResto;

    ImageView close;
    Dialog dialogComment, dialogRate;
    String mCurrentPhotoPath;
    private int userRate = 0;

    public RestaurantFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return Une nouvelle instance fragment de type  RestaurantFragment.
     */
    public static RestaurantFragment newInstance(Restaurant r) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESTO, r);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        restaurant = (Restaurant) args.getSerializable(ARG_RESTO);

        getSherlockActivity().setTitle("Restaurant");

        if(!restaurant.isFavoris())
            new RestaurantService(getSherlockActivity()).execute(restaurant.getId());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(_rootView == null){
            _rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);

            ((TextView)_rootView.findViewById(R.id.nom)).setText(restaurant.getNom());

            listViewCommentaire = (ListView) _rootView.findViewById(R.id.listViewCommentaire);

            nomResto = (TextView) _rootView.findViewById(R.id.nomResto);
            adresseResto = (TextView) _rootView.findViewById(R.id.adresseResto);
            numNoteResto = (TextView) _rootView.findViewById(R.id.numNoteResto);
            numCommentResto = (TextView) _rootView.findViewById(R.id.numCommentResto);
            userNoteResto = (TextView) _rootView.findViewById(R.id.userNoteResto);
            noteResto = (TextView) _rootView.findViewById(R.id.noteResto);



            _rootView.findViewById(R.id.rate_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!restaurant.isUserRater()){
                        showRateDialog();
                    }
                }
            });
            _rootView.findViewById(R.id.comment_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog();
                }
            });

            _rootView.findViewById(R.id.photo_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakePictureIntent();
                }
            });





        /*On configure les tab Liste et Carte*/
            tabHost = (TabHost) _rootView.findViewById(android.R.id.tabhost);
            tabHost.setup();

            final TabWidget tabWidget = tabHost.getTabWidget();
            final FrameLayout tabContent = tabHost.getTabContentView();
            tabHost.getTabWidget().setDividerDrawable(R.drawable.divider);

            // Get the original tab textviews and remove them from the viewgroup.
            TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
            for (int index = 0; index < tabWidget.getTabCount(); index++) {
                originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);

            }
            tabWidget.removeAllViews();

            // Ensure that all tab content childs are not visible at startup.
            for (int index = 0; index < tabContent.getChildCount(); index++) {
                tabContent.getChildAt(index).setVisibility(View.GONE);
            }

            // Create the tabspec based on the textview childs in the xml file.
            // Or create simple tabspec instances in any other way...
            for (int index = 0; index < originalTextViews.length; index++) {
                final TextView tabWidgetTextView = originalTextViews[index];
                final View tabContentView = tabContent.getChildAt(index);
                TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
                tabSpec.setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String tag) {
                        return tabContentView;
                    }
                });
                if (tabWidgetTextView.getBackground() == null) {
                    tabSpec.setIndicator(tabWidgetTextView.getText());
                } else {
                    tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
                }
                tabHost.addTab(tabSpec);
            }


            tabHost.setCurrentTab(0);
            initialize("Infos");

            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
                @Override
                public void onTabChanged(String tag) {

                    initialize(tag);

                }});

            for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
            {
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tv.setAllCaps(false);
                }
                tv.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL);
                // En dessous de l'API 11 HoneyComb, les tab ont un height trop grand, d'ou le code suivant pour la diviser par 1.7
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    tabHost.getTabWidget().getChildAt(i).getLayoutParams().height /=1.7;
                }
            }
        }else{
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }

        if(restaurant.isFavoris())
            setRestaurant(restaurant);


        return _rootView;
    }

    public void initialize(String tag){
        if(tag.equals("Infos")){
            _rootView.findViewById(R.id.rate_button).setVisibility(View.VISIBLE);
            _rootView.findViewById(R.id.comment_button).setVisibility(View.GONE);
            _rootView.findViewById(R.id.photo_button).setVisibility(View.GONE);
        }
        else if(tag.equals("Commentaires")){
            _rootView.findViewById(R.id.rate_button).setVisibility(View.GONE);
            _rootView.findViewById(R.id.comment_button).setVisibility(View.VISIBLE);
            _rootView.findViewById(R.id.photo_button).setVisibility(View.GONE);
        }
        else {
            _rootView.findViewById(R.id.rate_button).setVisibility(View.GONE);
            _rootView.findViewById(R.id.comment_button).setVisibility(View.GONE);
            _rootView.findViewById(R.id.photo_button).setVisibility(View.VISIBLE);
        }

        setSelectedTabColor();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setSelectedTabColor() {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getSherlockActivity().getResources().getColor(R.color.tab_unselected));
            ((TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title)).setTextColor(getSherlockActivity().getResources().getColor(R.color.tab_text_unselected));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getSherlockActivity().getResources().getColor(R.color.tab_selected));
        ((TextView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title)).setTextColor(getSherlockActivity().getResources().getColor(R.color.tab_text_selected));
    }

    @Override
    public void onResume() {
        super.onResume();
        getSherlockActivity().setTitle("Restaurant");

    }

    public void setRestaurant(final Restaurant restaurant) {
        this.restaurant = restaurant;
        adapter = new RestaurantCommentAdapter(getSherlockActivity(),
                R.layout.restaurant_comment_item, restaurant.getComments()
        );
        listViewCommentaire.setAdapter(adapter);

        if(restaurant.isUserRater())
            ((ImageView)_rootView.findViewById(R.id.rate_button)).setImageResource(R.drawable.user_rated);

        nomResto.setText(restaurant.getNom());
        adresseResto.setText(restaurant.getAdresse());
        numCommentResto.setText("Il y a "+restaurant.getNbCommentaire()+" avis disponible(s) sur ce restaurant");
        if(restaurant.isUserRater())
            userNoteResto.setText("Vous avez donné une note de "+restaurant.getUserRate()+" à ce restaurant");

        DecimalFormat df = new DecimalFormat("#.##");

        ((RatingBar)_rootView.findViewById(R.id.ratingBar)).setRating(restaurant.getNote().floatValue());
        ((RatingBar)_rootView.findViewById(R.id.ratingBar)).setEnabled(false);

        if(restaurant.getNote()!=0)
            noteResto.setText("Note FoodSquare : "+df.format(restaurant.getNote()));
        else
            noteResto.setText("Aucune note sur ce resto");

        // On initialise la gridView pour la Galerie Photo
        GridView gridView = (GridView) _rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new GalerieGridAdapter(getSherlockActivity(), restaurant.getGallerie()));

        // On gere l'ajout ou la suppression des restaurants en favoris
        final String action;
        ((ImageView) _rootView.findViewById(R.id.fav_image)).setVisibility(View.VISIBLE);
        if(restaurant.isFavoris()){
            ((ImageView) _rootView.findViewById(R.id.fav_image)).setImageResource(R.drawable.menu_fav);
            ((TextView) _rootView.findViewById(R.id.fav_text)).setText("Retirer de vos favoris");
            action = "delete";
        }else{
            ((ImageView) _rootView.findViewById(R.id.fav_image)).setImageResource(R.drawable.fav);
            ((TextView) _rootView.findViewById(R.id.fav_text)).setText("Ajouter à vos favoris");
            action = "add";
        }
        _rootView.findViewById(R.id.fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action.equals("add")) {
                    ((ImageView) _rootView.findViewById(R.id.fav_image)).setImageResource(R.drawable.menu_fav);
                    ((TextView) _rootView.findViewById(R.id.fav_text)).setText("Retirer de vos favoris");
                }else {
                    ((ImageView) _rootView.findViewById(R.id.fav_image)).setImageResource(R.drawable.fav);
                    ((TextView) _rootView.findViewById(R.id.fav_text)).setText("Ajouter à vos favoris");
                }
                new FavorisService(getSherlockActivity(), restaurant).execute(action);

            }
        });

    }

    public void showRateDialog(){
        if(dialogRate==null){
            dialogRate = new Dialog(getSherlockActivity(),R.style.PauseDialog);
            // Making sure there's no title.
            dialogRate.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogRate.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // Setting position of content, relative to window.
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialogRate.getWindow();
            lp.copyFrom(window.getAttributes());

            dialogRate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            // If user taps anywhere on the screen, dialog will be cancelled.
            dialogRate.setCancelable(true);
            // Setting the content using prepared XML layout file.
            dialogRate.setContentView(R.layout.restaurant_rate);
            (dialogRate.findViewById(R.id.note1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageView)view).setImageResource(R.drawable.note1_selected);
                    ((ImageView)dialogRate.findViewById(R.id.note2)).setImageResource(R.drawable.note2);
                    ((ImageView)dialogRate.findViewById(R.id.note3)).setImageResource(R.drawable.note3);
                    ((ImageView)dialogRate.findViewById(R.id.note4)).setImageResource(R.drawable.note4);
                    ((ImageView)dialogRate.findViewById(R.id.note5)).setImageResource(R.drawable.note5);
                    (dialogRate.findViewById(R.id.noter)).setVisibility(View.VISIBLE);
                    userRate = 1;
                }
            });
            (dialogRate.findViewById(R.id.note2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageView)view).setImageResource(R.drawable.note2_selected);
                    ((ImageView)dialogRate.findViewById(R.id.note1)).setImageResource(R.drawable.note1);
                    ((ImageView)dialogRate.findViewById(R.id.note3)).setImageResource(R.drawable.note3);
                    ((ImageView)dialogRate.findViewById(R.id.note4)).setImageResource(R.drawable.note4);
                    ((ImageView)dialogRate.findViewById(R.id.note5)).setImageResource(R.drawable.note5);
                    (dialogRate.findViewById(R.id.noter)).setVisibility(View.VISIBLE);
                    userRate = 2;
                }
            });
            (dialogRate.findViewById(R.id.note3)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageView)view).setImageResource(R.drawable.note3_selected);
                    ((ImageView)dialogRate.findViewById(R.id.note1)).setImageResource(R.drawable.note1);
                    ((ImageView)dialogRate.findViewById(R.id.note2)).setImageResource(R.drawable.note2);
                    ((ImageView)dialogRate.findViewById(R.id.note4)).setImageResource(R.drawable.note4);
                    ((ImageView)dialogRate.findViewById(R.id.note5)).setImageResource(R.drawable.note5);
                    (dialogRate.findViewById(R.id.noter)).setVisibility(View.VISIBLE);
                    userRate = 3;
                }
            });
            (dialogRate.findViewById(R.id.note4)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageView)view).setImageResource(R.drawable.note4_selected);
                    ((ImageView)dialogRate.findViewById(R.id.note1)).setImageResource(R.drawable.note1);
                    ((ImageView)dialogRate.findViewById(R.id.note2)).setImageResource(R.drawable.note2);
                    ((ImageView)dialogRate.findViewById(R.id.note3)).setImageResource(R.drawable.note3);
                    ((ImageView)dialogRate.findViewById(R.id.note5)).setImageResource(R.drawable.note5);
                    (dialogRate.findViewById(R.id.noter)).setVisibility(View.VISIBLE);
                    userRate = 4;
                }
            });
            (dialogRate.findViewById(R.id.note5)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageView)view).setImageResource(R.drawable.note5_selected);
                    ((ImageView)dialogRate.findViewById(R.id.note1)).setImageResource(R.drawable.note1);
                    ((ImageView)dialogRate.findViewById(R.id.note2)).setImageResource(R.drawable.note2);
                    ((ImageView)dialogRate.findViewById(R.id.note3)).setImageResource(R.drawable.note3);
                    ((ImageView)dialogRate.findViewById(R.id.note4)).setImageResource(R.drawable.note4);
                    (dialogRate.findViewById(R.id.noter)).setVisibility(View.VISIBLE);
                    userRate = 5;
                }
            });
            (dialogRate.findViewById(R.id.noter)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    (new RateService(getSherlockActivity(),restaurant.getId())).execute(Integer.toString(userRate));
                }
            });
            close =(ImageView) dialogRate.findViewById(R.id.closebutton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogRate();
                }
            });



        }
        dialogRate.show();
    }

    public void hideDialogRate(){
        dialogRate.dismiss();
    }

    public void showCommentDialog(){
        if(dialogComment==null){
            dialogComment = new Dialog(getSherlockActivity(),R.style.PauseDialog);
            // Making sure there's no title.
            dialogComment.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogComment.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // Setting position of content, relative to window.
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialogComment.getWindow();
            lp.copyFrom(window.getAttributes());

            dialogComment.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            // If user taps anywhere on the screen, dialog will be cancelled.
            dialogComment.setCancelable(true);
            // Setting the content using prepared XML layout file.
            dialogComment.setContentView(R.layout.restaurant_comment);

            final EditText input = (EditText)dialogComment.findViewById(R.id.commentInput);

            (dialogComment.findViewById(R.id.commenter)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (input.getText().toString().equals("") || input.getText().toString().length() < 2 ){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
                        builder.setMessage("Veuillez remplir correctement tous les champs")
                                .setPositiveButton("Ok", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        (new CommentService(getSherlockActivity(), restaurant.getId())).execute(input.getText().toString());
                    }


                }
            });
            close =(ImageView) dialogComment.findViewById(R.id.closebutton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogComment();
                }
            });



        }
        dialogComment.show();
    }

    public void hideDialogComment(){
        dialogComment.dismiss();
    }

    private void sendPhoto(Bitmap bitmap) throws Exception {
        new PhotoUploader(getSherlockActivity(), restaurant.getId()).execute(bitmap);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getSherlockActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("Upload photo", "photo path = " + mCurrentPhotoPath);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.i("Upload photo", "onActivityResult: " + this);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getSherlockActivity().getContentResolver(), Uri.parse("file://"+mCurrentPhotoPath));
                sendPhoto(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
