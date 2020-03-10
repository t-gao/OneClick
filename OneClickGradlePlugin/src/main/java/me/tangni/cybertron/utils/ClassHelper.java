package me.tangni.cybertron.utils;

import com.android.annotations.NonNull;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import org.gradle.api.Project;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassHelper {

    @NonNull
    public static URLClassLoader getClassLoader(@NonNull Project project,
                                                @NonNull TransformInvocation transformInvocation) throws MalformedURLException {
        return getClassLoader(project, transformInvocation.getInputs(), transformInvocation.getReferencedInputs());
    }

    @NonNull
    public static URLClassLoader getClassLoader(@NonNull Project project,
                                                @NonNull Collection<TransformInput> inputs,
                                                @NonNull Collection<TransformInput> referencedInputs) throws MalformedURLException {
        ImmutableList.Builder<URL> urls = new ImmutableList.Builder<>();
        String androidJarPath  = getAndroidJarPath(project);
        File file = new File(androidJarPath);
        URL androidJarURL = file.toURI().toURL();
        urls.add(androidJarURL);
        for (TransformInput totalInputs : Iterables.concat(inputs, referencedInputs)) {
            for (DirectoryInput directoryInput : totalInputs.getDirectoryInputs()) {
                if (directoryInput.getFile().isDirectory()) {
                    urls.add(directoryInput.getFile().toURI().toURL());
                }
            }
            for (JarInput jarInput : totalInputs.getJarInputs()) {
                if (jarInput.getFile().isFile()) {
                    urls.add(jarInput.getFile().toURI().toURL());
                }
            }
        }
        ImmutableList<URL> allUrls = urls.build();
        URL[] classLoaderUrls = allUrls.toArray(new URL[0]);
        return new URLClassLoader(classLoaderUrls);
    }

    /**
     * ${ANDROID_HOME}/platforms/android-${compileSdkVersion}/android.jar
     */
    private static String getAndroidJarPath(@NonNull Project project) {
//        AppExtension appExtension = (AppExtension)project.getProperties().get("android");
        BaseExtension extension = project.getExtensions().findByType(AppExtension.class);
        if (extension == null) {
            extension = project.getExtensions().findByType(LibraryExtension.class);
        }

        if (extension == null) {
            extension = project.getExtensions().findByType(BaseExtension.class);
        }
        if (extension != null) {
            String sdkDirectory = extension.getSdkDirectory().getAbsolutePath();
            String compileSdkVersion = extension.getCompileSdkVersion();
            sdkDirectory = sdkDirectory + File.separator + "platforms" + File.separator;
            return sdkDirectory + compileSdkVersion + File.separator + "android.jar";
        }
        return "";
    }

    public static Class<?> loadClass(@NonNull ClassLoader classLoader, @NonNull String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void findAllInterfaces(@NonNull Class<?> clazz, @NonNull List<String> outInterfaces) {

        if (clazz == Object.class) return;

        if (clazz.isInterface()) {
            outInterfaces.add(clazz.getTypeName());
        } else {
            Class<?>[] itfs = clazz.getInterfaces();
            int len = itfs == null ? 0 : itfs.length;
            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    outInterfaces.add(itfs[i].getTypeName());
                }
            }
        }

        findAllInterfaces(clazz.getSuperclass(), outInterfaces);
    }

    public static List<String> getAllInterfaces(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<?> clazz = loadClass(classLoader, className);
        if (clazz == null) return null;

        List<String> outInterfaces = new ArrayList<String>();
        findAllInterfaces(clazz, outInterfaces);
        return outInterfaces;
    }
}
