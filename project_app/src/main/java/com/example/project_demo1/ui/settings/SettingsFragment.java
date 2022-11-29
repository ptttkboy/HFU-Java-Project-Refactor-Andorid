package com.example.project_demo1.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_demo1.R;

/**
 * 測試用頁面
 */
public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
//    private ActivityResultLauncher a;
//    private Bitmap bitmap;
//    private ExecutorService executorService;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        executorService = MyApplication.getExecutorService();
//
//        Log.d(TAG, "onCreate: ");
//        //
//        a = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                // System.out.println(result);
//                try {
//                    InputStream is = getActivity().getContentResolver().openInputStream(result);
//
//                    byte[] src = IOUtils.toByteArray(is);
//                    String encoded = Base64.encodeToString(src, Base64.NO_WRAP);
//
//                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result);
//                    ImageView iv = getActivity().findViewById(R.id.setting_img_test);
//                    iv.setImageBitmap(bitmap);
//
//                    PictureUtils pictureUtils = new PictureUtils();
//                    String encoded2 = pictureUtils.compressContentUri(result, getActivity());
//                    ImageView iv2 = getActivity().findViewById(R.id.setting_img_test222);
//                    iv2.setImageBitmap(pictureUtils.base64ToBitmap(encoded2));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Button connecting_test_btn = view.findViewById(R.id.connecting_test_btn);
//        connecting_test_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String jsonQuery = "{\"inputAccount\":\"123\", \"inputPassword\":\"123\"}";
//                        try {
//                            String encodedJsonQuery = URLEncoder.encode(jsonQuery, "UTF-8");
//                            new HttpUtils().doGETRequest(encodedJsonQuery, "MemberController");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//        });
//
//        Button setting_test_btn = view.findViewById(R.id.setting_test_btn);
//        setting_test_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                a.launch("image/*");
//            }
//        });

        /* -----------------login test ------------------ */

//        TextView login_test_tv = view.findViewById(R.id.login_test_tv);
//
//        Button btn_login_test = view.findViewById(R.id.btn_login_test);
//        btn_login_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Map<String, String> response = new LoginTest().loginTest("123", "123");
//
//                        int statusCode = Integer.parseInt(response.get("statusCode"));
//                        String responseBody = response.get("responseBody");
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if(statusCode == 200) {
//
//                                    login_test_tv.setText(responseBody);
//                                    Toast.makeText(getActivity(), "登入成功", Toast.LENGTH_SHORT).show();
//
//                                } else if (statusCode == 400) {
//
//                                    Toast.makeText(getActivity(), "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                    Toast.makeText(getActivity(), "未知的錯誤", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                    }
//                });
//            }
//        });
//
//        /* ------------------ Image download test --------------- */
//
//        Button setting_img_download_btn = view.findViewById(R.id.setting_img_download_btn);
//
//        setting_img_download_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        String testUrl = "https://i.imgur.com/qu6VCEe.jpeg";
//
//                        Bitmap pekora = new PictureUtils().downloadImgAsBitmap(testUrl);
//
//                        if(getActivity() == null) return;
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ImageView setting_downloadedImg = view.findViewById(R.id.setting_downloadedImg);
//                                setting_downloadedImg.setImageBitmap(pekora);
//                            }
//                        });
//                    }
//                });
//            }
//        });



    }
}