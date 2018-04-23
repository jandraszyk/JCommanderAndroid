package com.example.janek.jcommanderandroid;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class leftListFragment extends Fragment {

    private TextView mTextMessage;
    private ListView fileList;
    private MenuItem menuCopy;
    private MenuItem menuDelete;
    private MenuItem menuLanguage;
    private MenuItem menuMove;
    private MenuItem menuSort;
    private Menu menu;
    private BottomNavigationView navigation;
    private boolean ascending = true;

    private ArrayList<FileModel> leftFileList;
    private FileModelAdapter fileListAdapter;
    private FileModel leftFiles;
    private FileModel rightFiles;
    private ArrayList<FileModel> selectedFile = new ArrayList<>();

    private OnLeftFragmentInteractionListener mListener;

    private String LIST_INSTANCE_STATE = "save";

    String path1 = "/storage/emulated/0";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_copy:

                    if(selectedFile.size() <= 0) {
                        Toast.makeText(getContext(), R.string.select_files,Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    for (FileModel fileModel : selectedFile) {
                        System.out.println("Selected left file: " + fileModel.getFileName());
                    }
                    AlertDialog.Builder copyDialog = new AlertDialog.Builder(getActivity());
                    copyDialog.setTitle(R.string.wanna_copy)
                            .setPositiveButton(R.string.copy_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CopyFileTask copyFileTask = new CopyFileTask();
                                    copyFileTask.init(selectedFile.get(0).getCurrentDirectory(), rightFiles.getCurrentDirectory(), getContext());
                                    copyFileTask.execute();
                                    selectedFile.clear();
                                    fileListAdapter.notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton(R.string.copy_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    copyDialog.create().show();
                    return true;
                case R.id.navigation_delete:
                    if(selectedFile.size() <= 0) {
                        Toast.makeText(getContext(), R.string.select_files,Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    for (FileModel fileModel : selectedFile) {
                        System.out.println("Selected left file: " + fileModel.getFileName());
                    }
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
                    deleteDialog.setTitle(R.string.wanna_delete)
                            .setPositiveButton(R.string.copy_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DeleteFileTask deleteFileTask = new DeleteFileTask();
                                    deleteFileTask.init(selectedFile.get(0).getCurrentDirectory(), getContext());
                                    deleteFileTask.execute();
                                    fileListAdapter.remove(selectedFile.get(0));
                                    fileListAdapter.notifyDataSetChanged();
                                    selectedFile.clear();
                                }
                            })
                            .setNegativeButton(R.string.copy_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    fileListAdapter.notifyDataSetChanged();
                    deleteDialog.create().show();
                    return true;
                case R.id.navigation_move:
                    if(selectedFile.size() <= 0) {
                        Toast.makeText(getContext(), R.string.select_files,Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    for (FileModel fileModel : selectedFile) {
                        System.out.println("Selected left file: " + fileModel.getFileName());
                    }
                    AlertDialog.Builder moveDialog = new AlertDialog.Builder(getActivity());
                    moveDialog.setTitle(R.string.wanna_move)
                            .setPositiveButton(R.string.copy_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MoveFileTask moveFileTask = new MoveFileTask();
                                    moveFileTask.init(selectedFile.get(0).getCurrentDirectory(), rightFiles.getCurrentDirectory(), getContext(),fileListAdapter);
                                    moveFileTask.execute();
                                    fileListAdapter.remove(selectedFile.get(0));
                                    fileListAdapter.notifyDataSetChanged();
                                    selectedFile.clear();
                                }
                            })
                            .setNegativeButton(R.string.copy_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    fileListAdapter.notifyDataSetChanged();
                    moveDialog.create().show();
                    return true;
                case R.id.navigation_sort:
                    AlertDialog.Builder sortingDialog = new AlertDialog.Builder(getActivity());
                    sortingDialog.setTitle(R.string.sort_method)
                            .setPositiveButton(R.string.sort_name, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(ascending) {
                                        ascending = false;
                                        Collections.sort(leftFileList,  new Comparator<FileModel>() {
                                            @Override
                                            public int compare(FileModel fileModel, FileModel t1) {
                                                if(fileModel.getFileName().equals("..") || t1.getFileName().equals("..")) {
                                                    return 0;
                                                } else {
                                                    fileListAdapter.notifyDataSetChanged();
                                                    return fileModel.getFileName().compareToIgnoreCase(t1.getFileName());
                                                }
                                            }
                                        });
                                    } else {
                                        ascending = true;
                                        Collections.sort(leftFileList, Collections.reverseOrder(new Comparator<FileModel>() {
                                            @Override
                                            public int compare(FileModel fileModel, FileModel t1) {
                                                if(fileModel.getFileName().equals("..") || t1.getFileName().equals("..")) {
                                                    return 0;
                                                } else {
                                                    fileListAdapter.notifyDataSetChanged();
                                                    return fileModel.getFileName().compareToIgnoreCase(t1.getFileName());
                                                }
                                            }
                                        }));
                                    }
                                }
                            })
                            .setNegativeButton(R.string.sort_date, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(ascending) {
                                        ascending = false;
                                        Collections.sort(leftFileList, new Comparator<FileModel>() {
                                            @Override
                                            public int compare(FileModel fileModel, FileModel t1) {
                                                if(fileModel.getFileLastModified().equals("") || t1.getFileLastModified().equals("")) {
                                                    return 0;
                                                } else {
                                                    fileListAdapter.notifyDataSetChanged();
                                                    return fileModel.getFileLastModified().compareToIgnoreCase(t1.getFileLastModified());
                                                }
                                            }
                                        });
                                    } else {
                                        ascending = true;
                                        Collections.sort(leftFileList, Collections.reverseOrder(new Comparator<FileModel>() {
                                            @Override
                                            public int compare(FileModel fileModel, FileModel t1) {
                                                if(fileModel.getFileLastModified().equals("") || t1.getFileLastModified().equals("")) {
                                                    return 0;
                                                } else {
                                                    fileListAdapter.notifyDataSetChanged();
                                                    return fileModel.getFileLastModified().compareToIgnoreCase(t1.getFileLastModified());
                                                }
                                            }
                                        }));
                                    }
                                }
                            })
                            .setNeutralButton(R.string.sort_size, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(ascending) {
                                        ascending = false;
                                        Collections.sort(leftFileList, new Comparator<FileModel>() {
                                            @Override
                                            public int compare(FileModel fileModel, FileModel t1) {
                                                if(fileModel.getFileSize().equals("") || t1.getFileSize().equals("")) {
                                                    return 0;
                                                } else {
                                                    fileListAdapter.notifyDataSetChanged();
                                                    return fileModel.getFileSize().compareToIgnoreCase(t1.getFileSize());
                                                }
                                            }
                                        });
                                    } else {
                                        ascending = true;
                                        Collections.sort(leftFileList, Collections.reverseOrder(new Comparator<FileModel>() {
                                            @Override
                                            public int compare(FileModel fileModel, FileModel t1) {
                                                if(fileModel.getFileSize().equals("") || t1.getFileSize().equals("")) {
                                                    return 0;
                                                } else {
                                                    fileListAdapter.notifyDataSetChanged();
                                                    return fileModel.getFileSize().compareToIgnoreCase(t1.getFileSize());
                                                }
                                            }
                                        }));
                                    }
                                }
                            });
                    sortingDialog.create().show();
                    return true;
                case R.id.navigation_language:
                    AlertDialog.Builder languageDialog = new AlertDialog.Builder(getActivity());
                    languageDialog
                            .setTitle(R.string.language_change)
                            .setPositiveButton(R.string.language_polish, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Locale locale = new Locale("PL");
                                    Locale.setDefault(locale);
                                    Configuration configuration = new Configuration();
                                    configuration.setLocale(locale);
                                    getActivity().getBaseContext().getResources().updateConfiguration(configuration,getActivity().getBaseContext().getResources().getDisplayMetrics());
                                    updateList();
                                    onConfigurationChanged(configuration);

                                }
                            })
                            .setNegativeButton(R.string.language_english, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Locale locale = new Locale("EN");
                                    Locale.setDefault(locale);
                                    Configuration configuration = new Configuration();
                                    configuration.setLocale(locale);
                                    getActivity().getApplicationContext().getResources().updateConfiguration(configuration,getActivity().getApplicationContext().getResources().getDisplayMetrics());
                                    updateList();
                                    onConfigurationChanged(configuration);
                                }
                            });
                    languageDialog.create().show();
                    return true;
            }
            return false;
        }
    };

    public leftListFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            File right = new File(bundle.getString("RIGHT_DIR","/storage"));
            rightFiles = new FileModel(right);
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            File fileRestore = new File(savedInstanceState.getString(LIST_INSTANCE_STATE, "/storage/emulated/0"));
            leftFiles = new FileModel(fileRestore);
            leftFileList = leftFiles.getFileListModel();
            fileListAdapter = new FileModelAdapter(getActivity(),R.layout.list_file,leftFileList);
            fileList.setAdapter(fileListAdapter);
            fileListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnLeftFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_left_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTextMessage = view.findViewById(R.id.leftDirTxt);
        fileList = view.findViewById(R.id.leftFileListView);
        fileList.setSaveEnabled(true);
        File directory = new File(path1);
        leftFiles = new FileModel(directory);
        leftFileList = leftFiles.getFileListModel();
        mTextMessage.setText(leftFiles.getCurrentDirectory().getAbsolutePath());


        fileListAdapter = new FileModelAdapter(getActivity(),R.layout.list_file,leftFileList);
        fileList.setAdapter(fileListAdapter);
        fileListAdapter.notifyDataSetChanged();
        navigation =  getActivity().findViewById(R.id.navigation);
        menu = navigation.getMenu();
        menuCopy = menu.findItem(R.id.navigation_copy);
        menuDelete = menu.findItem(R.id.navigation_delete);
        menuLanguage = menu.findItem(R.id.navigation_language);
        menuMove = menu.findItem(R.id.navigation_move);
        menuSort = menu.findItem(R.id.navigation_sort);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fileList.setOnItemClickListener(mOnItemClickListener);
        fileList.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        menuSort.setTitle(R.string.title_sort);
        menuLanguage.setTitle(R.string.title_language);
        menuDelete.setTitle(R.string.title_delete);
        menuCopy.setTitle(R.string.title_copy);
        menuMove.setTitle(R.string.title_move);
        fileList.setAdapter(fileListAdapter);
        fileListAdapter.notifyDataSetChanged();

    }

    public void updateRightDir(FileModel fileModel) {
        rightFiles = fileModel;
        System.out.println("Left: " + rightFiles.getCurrentDirectory().getAbsolutePath());
    }

    public interface OnLeftFragmentInteractionListener {
        public void onLeftFragmentInteraction(FileModel file);
    }



    public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            FileModel fileModel = fileListAdapter.getItem(position);
            if(fileModel.getCurrentDirectory().isDirectory()) {
                mTextMessage.setText(fileModel.getCurrentDirectory().getAbsolutePath());
                leftFileList = fileModel.getFileListModel();
                fileListAdapter.clear();
                fileListAdapter = new FileModelAdapter(getActivity(), R.layout.list_file, leftFileList);
                fileList.setAdapter(fileListAdapter);
                fileListAdapter.notifyDataSetChanged();
                System.out.println(fileModel.getFileName());
                if(mListener != null) {
                    mListener.onLeftFragmentInteraction(fileModel);
                }
            }
        }
    };

    public AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            if(fileListAdapter.getItem(position).getFileName().equals("..")) {
                Toast.makeText(getContext(), R.string.cant_select,Toast.LENGTH_SHORT).show();
                return true;
            } else {
                if(!fileListAdapter.getItem(position).isSelected()) {
                    adapterView.getChildAt(position - fileList.getFirstVisiblePosition()).setBackgroundColor(Color.GRAY);
                    fileListAdapter.getItem(position).setSelected(true);
                    selectedFile.add(fileListAdapter.getItem(position));
                } else {
                    fileListAdapter.getItem(position).setSelected(false);
                    adapterView.getChildAt(position - fileList.getFirstVisiblePosition()).setBackgroundColor(Color.TRANSPARENT);
                    selectedFile.remove(fileListAdapter.getItem(position));
                }
                return true;
            }

        }
    };

    public void updateList(){
        leftFileList = leftFiles.getFileListModel();
        fileListAdapter.clear();
        fileListAdapter = new FileModelAdapter(getActivity(), R.layout.list_file,leftFileList);
        fileList.setAdapter(fileListAdapter);
        fileListAdapter.notifyDataSetChanged();
    }


}
