package com.sig.baubau.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.adapter.adpList;
import com.sig.baubau.model.modelKec;
import com.sig.baubau.model.modelList;
import com.sig.baubau.model.modelLongsor;
import com.sig.baubau.model.modelPosko;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongsorFragment extends Fragment {
    private static final int PATTERN_DASH_LENGTH = 10;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH);
    private static final PatternItem DOT = new Dot();
    private static final List<PatternItem> PATTERN_ITEMS = Arrays.asList(DOT, DASH);
    ArrayList<Double> costs = new ArrayList<>();
    Polygon wilaya = null;
    Marker markers;
    private DatabaseReference db;
    View viewLayout;
    private modelLongsor model;
    FloatingActionButton fab;
    private ArrayList<modelLongsor> list;
    private ArrayList<String> longitude = new ArrayList<>();
    private ArrayList<String> kecamatan = new ArrayList<>();
    ArrayList<String> kode = new ArrayList<>();
    ArrayList<String> latitude = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();
    private ArrayList<String> kelurahan = new ArrayList<>();
    private static ArrayList<String> hasil = new ArrayList<>();

    GoogleMap gMap;
    GoogleMap googleMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap gM) {
            googleMap = gM;
            gMap = gM;

            mapON();

            fab = viewLayout.findViewById(R.id.fabLongsor);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mapON();
                    if (markerList.size() > 0) {
                        markerList.clear();
                        gMap.clear();
                    } else {
                        viewMarkers();
                    }
                }
            });

            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    Toast.makeText(viewLayout.getContext(), marker.getTag().toString(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    };

    private void mapON() {
        LatLng kota = new LatLng(-5.434039, 122.667554);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kota));
        CameraPosition position = CameraPosition.builder()
                .target(kota)
                .zoom(11.3f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        db = FirebaseDatabase.getInstance().getReference("longsor");
        list = new ArrayList<>();

        ArrayList<modelLongsor> pusat1 = new ArrayList<>();
        ArrayList<modelLongsor> pusat2 = new ArrayList<>();

        final ArrayList<String>[] stepOne = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] stepTwo = new ArrayList[]{new ArrayList<>()};

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    model = new modelLongsor();
                    model.setTidak(Double.parseDouble(ds.child("tidak").getValue().toString()));
                    model.setRendah(Double.parseDouble(ds.child("rendah").getValue().toString()));
                    model.setSedang(Double.parseDouble(ds.child("sedang").getValue().toString()));
                    model.setKode(ds.getKey());
                    list.add(model);
                }

                pusat2.add(list.get(7));
                pusat2.add(list.get(9));
                pusat2.add(list.get(36));

                pusat1.add(list.get(9));
                pusat1.add(list.get(33));
                pusat1.add(list.get(34));

                stepOne[0] = manhattan(pusat1);
                stepTwo[0] = manhattan(pusat2);

                double costLama = costs.get(0);
                double costBaru = costs.get(1);

                if (costBaru > costLama) {
                    hasil = stepTwo[0];
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("kel");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kelurahan.clear();
                kode.clear();
                longitude.clear();
                latitude.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    kode.add(ds.getKey());
                    kelurahan.add(ds.child("nama").getValue().toString());
                    latitude.add(ds.child("lat").getValue().toString());
                    longitude.add(ds.child("lng").getValue().toString());
                }

                if (hasil.size() > 0) {
                    for (int i=0; i<latitude.size(); i++) {
                        String[] lat1 = latitude.get(i).split(",");
                        String[] lng1 = longitude.get(i).split(",");
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        for (int j=0; j<lat1.length; j++) {
                            latLngs.add(new LatLng(Double.parseDouble(lat1[j]), Double.parseDouble(lng1[j])));
                        }

                        PolygonOptions options = new PolygonOptions().clickable(true).addAll(latLngs);
                        if (hasil.get(i).trim().equals("Aman")) {
                            options.fillColor(Color.parseColor("#40FFFFFF"));
                        } else
                        if (hasil.get(i).trim().equals("Rendah")) {
                            options.fillColor(Color.parseColor("#4000FF00"));
                        } else
                        if (hasil.get(i).trim().equals("Sedang")) {
                            options.fillColor(Color.parseColor("#40FFFF00"));
                        }
                        options.strokeWidth(0);
                        Polygon pol = googleMap.addPolygon(options);
                        pol.setTag(indeksAncaman(hasil.get(i)) + "\n" +
                                kode.get(i) + "\n" + kelurahan.get(i) + "\n" +
                                namaKec(kode.get(i).substring(0, 6)));
                        wilayahKec(googleMap);
                        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                            @Override
                            public void onPolygonClick(@NonNull Polygon polygon) {
                                if (wilaya != null) {
                                    wilaya.setStrokeWidth(0);
                                    wilaya.setStrokePattern(null);
                                }
                                wilaya = polygon;
                                wilaya.setStrokeWidth(3);
                                wilaya.setStrokePattern(PATTERN_ITEMS);
                                String[] getTAG = wilaya.getTag().toString().split("\n");
                                showBottom(getTAG[1], getTAG[2], getTAG[3], getTAG[0]);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String indeksAncaman(String indeks) {
        if (indeks.equals("Aman")) return "Tidak Berpotensi";
        else return "Ancaman " + indeks;
    }

    private void viewMarkers() {
        DatabaseReference dbPosko = FirebaseDatabase.getInstance().getReference("posko");
        dbPosko.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                markerList.clear();
                int n = 1;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelPosko model = ds.getValue(modelPosko.class);
                    double lat = Double.parseDouble(model.getLat());
                    double lng = Double.parseDouble(model.getLng());
                    markers = gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.camp)));
                    markers.setTag(model.getKode() + "\n" + model.getNama());
                    markerList.add(markers);
                    n++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottom(String kode, String kel, String kec, String indeks) {
        final Dialog bottomSheetDialog = new Dialog(viewLayout.getContext());
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layoutdg);
        TextView txKode = bottomSheetDialog.findViewById(R.id.bot_kode);
        TextView txKel = bottomSheetDialog.findViewById(R.id.bot_kel);
        TextView txKec = bottomSheetDialog.findViewById(R.id.bot_kec);
        TextView txIndeks = bottomSheetDialog.findViewById(R.id.bot_status);

        txKode.setText(kode);
        txKec.setText(kec);
        txKel.setText(kel);
        txIndeks.setText(indeks);
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        bottomSheetDialog.show();
    }

    private String namaKec(String kode) {
        if (kode.equals("747201")) return "Betoambari";
        if (kode.equals("747202")) return "Bungi";
        if (kode.equals("747204")) return "Lea-Lea";
        if (kode.equals("747203")) return "Kokalukuna";
        if (kode.equals("747205")) return "Murhum";
        if (kode.equals("747206")) return "Wolio";
        if (kode.equals("747207")) return "Sorawolio";
        if (kode.equals("747208")) return "Batupoaro";
        else return kode;
    }

    private void wilayahKec(GoogleMap googleMap) {
        LatLng kota = new LatLng(-5.434039, 122.667554);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kota));
        CameraPosition position = CameraPosition.builder()
                .target(kota)
                .zoom(11.3f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        ArrayList<Integer> warna = new ArrayList<>();
        warna.add(getResources().getColor(R.color.wil_betoambari));
        warna.add(getResources().getColor(R.color.wil_bungi));
        warna.add(getResources().getColor(R.color.wil_kokalukuna));
        warna.add(getResources().getColor(R.color.wil_lealea));
        warna.add(getResources().getColor(R.color.wil_murhum));
        warna.add(getResources().getColor(R.color.wil_wolio));
        warna.add(getResources().getColor(R.color.wil_sorawolio));
        warna.add(getResources().getColor(R.color.wil_batupoaro));

        DatabaseReference dbKec = FirebaseDatabase.getInstance().getReference("kecamatan");
        ArrayList<String> titikLat = new ArrayList<>();
        ArrayList<String> titikLng = new ArrayList<>();
        ArrayList<String> kodeKec = new ArrayList<>();
        ArrayList<String> namaKec = new ArrayList<>();
        dbKec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titikLat.clear();
                titikLng.clear();
                kodeKec.clear();
                namaKec.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelKec model = ds.getValue(modelKec.class);
                    kodeKec.add(model.getKode());
                    namaKec.add(model.getNama());
                    titikLat.add(model.getLat());
                    titikLng.add(model.getLng());
                }

                if (titikLat.size() > 0) {
                    for (int i=0; i<titikLat.size(); i++) {
                        String[] lat1 = titikLat.get(i).split(",");
                        String[] lng1 = titikLng.get(i).split(",");
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        for (int j=0; j<lat1.length; j++) {
                            latLngs.add(new LatLng(Double.parseDouble(lat1[j]), Double.parseDouble(lng1[j])));
                        }

                        PolygonOptions options = new PolygonOptions().clickable(false).addAll(latLngs)
                                .strokeColor(warna.get(i))
                                .strokeWidth(3);
                        Polygon pol = googleMap.addPolygon(options);
                        pol.setTag(kodeKec.get(i) + "\n" + namaKec.get(i));

                        /*googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                            @Override
                            public void onPolygonClick(@NonNull Polygon polygon) {
                                if (wilaya != null) {
                                    wilaya.setStrokeWidth(0);
                                    wilaya.setStrokePattern(null);
                                }
                                wilaya = polygon;
                                String[] getTAG = wilaya.getTag().toString().split("\n");
                                List<modelList> modelLists =new ArrayList<>();
                                for (int w=0; w<kode.size(); w++) {
                                    if (kode.get(w).substring(0, 6).equals(getTAG[0])) {
                                        String mn = kode.get(w) + " - " + kelurahan.get(w);
                                        modelLists.add(new modelList(mn, hasil.get(w)));
                                    }
                                }

                                showBot(getTAG[0], getTAG[1], modelLists);
                            }
                        });*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBot(String kode, String kec, List<modelList> data) {
        final Dialog bottomSheetDialog = new Dialog(viewLayout.getContext());
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layoutdgg);
        TextView txKode = bottomSheetDialog.findViewById(R.id.bottom_kode);
        TextView txKec = bottomSheetDialog.findViewById(R.id.bottom_kec);

        txKode.setText(kode);
        txKec.setText(kec);

        RecyclerView rv = bottomSheetDialog.findViewById(R.id.rvList);
        adpList adp = new adpList(data);
        LinearLayoutManager ly = new LinearLayoutManager(viewLayout.getContext());
        rv.setLayoutManager(ly);
        rv.setAdapter(adp);

        if (bottomSheetDialog.getWindow() != null) {
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewLayout = inflater.inflate(R.layout.fragment_longsor, container, false);

        return viewLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapLongsor);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private ArrayList<String> manhattan(ArrayList<modelLongsor> pusatCentroid) {
        double[][] centroid = new double[pusatCentroid.size()][3];
        for (int i=0; i<pusatCentroid.size(); i++) {
            centroid[i][0] = pusatCentroid.get(i).getTidak();
            centroid[i][1] = pusatCentroid.get(i).getRendah();
            centroid[i][2] = pusatCentroid.get(i).getSedang();
        }

        double[][] dataSampel = new double[list.size()][3];
        for (int i=0; i<list.size(); i++) {
            dataSampel[i][0] = list.get(i).getTidak();
            dataSampel[i][1] = list.get(i).getRendah();
            dataSampel[i][2] = list.get(i).getSedang();
        }

        double[][] dist = distance(dataSampel, centroid);
        double[][] dis = new double[list.size()][3];

        for (int i=0; i<list.size(); i++) {
            for (int j=0; j<3; j++) {
                dis[i][j] = dist[i][j];
            }
        }

        ArrayList<String> indeks = new ArrayList<>();
        ArrayList<Double> in = new ArrayList<>();

        for (int i=0; i<list.size(); i++) {
            double[] urut = new double[3];
            urut[0] = dis[i][0];
            urut[1] = dis[i][1];
            urut[2] = dis[i][2];
            String[] an = {"Aman", "Rendah", "Sedang"};
            for (int x=0; x<urut.length; x++) {
                for (int y=x; y<urut.length; y++) {
                    if (urut[x] > urut[y]) {
                        double tmpNilai = urut[x];
                        urut[x] = urut[y];
                        urut[y] = tmpNilai;

                        String tmpK = an[x];
                        an[x] = an[y];
                        an[y] = tmpK;
                    }
                }
            }
            in.add(urut[0]);
            indeks.add(an[0]);
        }
        costs.add(getTotal(in));
        return indeks;
    }

    private double[][] distance(double[][] data, double[][] cluster) {
        double[][] hasil = new double[data.length][3];
        for (int h=0; h<3; h++) {
            for (int i=0; i<data.length; i++) {
                for (int j=0; j<3; j++) {
                    hasil[i][h] += Math.abs(data[i][j] - cluster[h][j]);
                }
            }
        }
        return hasil;
    }

    private double getTotal(ArrayList<Double> nilai) {
        double n = 0;
        for (int i=0; i<nilai.size(); i++) {
            n = n + nilai.get(i);
        }
        return n;
    }
}