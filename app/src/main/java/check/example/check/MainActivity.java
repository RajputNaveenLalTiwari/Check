package check.example.check;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final int RUNTIME_PERMISSION_REQUEST_CODE = 5;
    private static final int REQUEST_CODE_FOR_DOCUMENT_TREE = 5;
    private static final String TAG = "MainActivity";
    private Context context;
    private ImageView image;

    private String externalStoragePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
//        init();
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            if(checkMultiplePermissions())
            {
                init();
            }
            else
            {
                Toast.makeText(context,"Need Permission To Work With This Application",Toast.LENGTH_LONG).show();
            }
        }*/
        //promptStorageAccessFrameWork();

//        delete("file:///storage/C090-10FF/.sridhargoud/My%20Novel.txt");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        PrintHelper printHelper = new PrintHelper(context);
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap("Naveen",bitmap);
    }

    public void promptStorageAccessFrameWork()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent,REQUEST_CODE_FOR_DOCUMENT_TREE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Uri uri = data.getData();
                getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                DocumentFile documentFile = DocumentFile.fromTreeUri(context,uri);
                DocumentFile documentFile1 = DocumentFile.fromTreeUri(context,uri);
                Log.i(TAG, "Found file " + documentFile1.getName() + " with size " + documentFile1.length());
                /*for (DocumentFile file:documentFile.listFiles())
                {
                    Log.i(TAG, "Found file " + file.getName() + " with size " + file.length());
                }*/
                /*try {
                    image.setImageBitmap(getBitmapFromUri(documentFile1.getUri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                DocumentFile newFile = documentFile1.createFile("text/plain", "My Novel");
                OutputStream out = null;
                try {
                    out = getContentResolver().openOutputStream(newFile.getUri());
                    out.write("A long time ago...".getBytes());
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private Bitmap getBitmapFromUri(Uri uri ) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    public void readFromExternalStorage(String path)
    {
        Uri uri = Uri.parse(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            File file1 = new File(path);
//            isDirectory(file1);
            DocumentFile documentFile = DocumentFile.fromFile(file1);
            isDirectory(documentFile);
            /*DocumentFile documentFile = DocumentFile.fromFile(file1);
            for (DocumentFile file:documentFile.listFiles())
            {
                Log.i(TAG, "Found file " + file.getName() + " with size " + file.length());
            }*/
        }
    }

    public void isDirectory(File file)
    {
        DocumentFile documentFile = DocumentFile.fromFile(file);
        if(documentFile!= null)
        {
            for (DocumentFile documentFile1:documentFile.listFiles())
            {
                if(documentFile1.isDirectory())
                {
//                    File file1 = new File(documentFile1.getUri().toString());
//                    isDirectory(file1);
                    Log.i(TAG,documentFile1.getName());
                }
                else
                {
//                    Log.i(TAG,documentFile1.getName());
                }
            }
        }
    }

    public void isDirectory(DocumentFile documentFile)
    {
        if(documentFile!=null)
        {
            for (DocumentFile documentFile1:documentFile.listFiles())
            {
                if(documentFile1.isDirectory())
                {
//                    Log.i(TAG,"Directory"+documentFile1.getName());
                    isDirectory(documentFile1);
                }
                else
                {
                   /* if(documentFile1.getUri().equals(Uri.parse("file:///storage/C090-10FF/.sridhargoud/My%20Novel.txt")))
                    {
                        Log.i(TAG,"Document File Name = "+documentFile1.getUri());
//                        delet(documentFile1.getUri());
                    }*/
                    if(documentFile1.getUri().equals(Uri.parse("file:///storage/C090-10FF/.sridhargoud/Cut-6923.png")))
                    {
                        Log.i(TAG, "Document File Name = " + documentFile1.getUri());
//                        delet(documentFile1.getUri());
                        del(documentFile1);
                        /*try
                        {
                            image.setImageBitmap(getBitmapFromUri(documentFile1.getUri()));
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }*/
                    }


                }
            }
        }
    }

    public void del(DocumentFile documentFile)
    {
        documentFile.canRead();
        documentFile.canWrite();
        String fileName = getFileName(documentFile.getUri().getPath());
        Toast.makeText(context,fileName,Toast.LENGTH_LONG).show();
        DocumentFile documentFile1 = documentFile.findFile(fileName);
//        documentFile1.canRead();
//        documentFile1.canWrite();
        Toast.makeText(context,"Hello"+documentFile1,Toast.LENGTH_LONG).show();
        boolean isDelete = documentFile.delete();
        if(isDelete)
        {
            Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }

    public static String getFileName(String fullPath) {
        return fullPath.substring(fullPath.lastIndexOf(File.separator) + 1);
    }

    public void delete(String path)
    {
        Uri uri = Uri.parse(path);
        File file = new File(path);
        DocumentFile documentFile = DocumentFile.fromFile(file);
        if(documentFile.delete())
        {
            Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }

    public void delet(Uri uri)
    {
        File file = new File(uri.toString());
        DocumentFile documentFile = DocumentFile.fromFile(file);
        if(documentFile.delete())
        {
            Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            if(DocumentsContract.deleteDocument(getContentResolver(),uri))
            {
                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        }*/
    }

    private boolean checkMultiplePermissions()
    {
        int readPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE);
        int wirtePermission = ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissionList = new ArrayList<>();
        if (readPermission != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (wirtePermission != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty())
        {
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]), RUNTIME_PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case RUNTIME_PERMISSION_REQUEST_CODE:
                if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    Toast.makeText(context,"Now You Got Permission To Work With This Application",Toast.LENGTH_LONG).show();
                    init();
                }
                else
                {
                    Toast.makeText(context,"Need Permission To Work With This Application",Toast.LENGTH_LONG).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }


    }

    private void init()
    {
        image = (ImageView) findViewById(R.id.image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            File[] files = getExternalFilesDirs(null);
            for (File file:files)
            {
                if(file!=null)
                {
                    externalStoragePath = files[1].getAbsolutePath();
                    String[] split = externalStoragePath.split("/Android/data/"+getPackageName()+"/files");
                    for (String s:split)
                    {
                        externalStoragePath = s;
                    }
                }

            }
            readFromExternalStorage(externalStoragePath);

        }
    }
}
