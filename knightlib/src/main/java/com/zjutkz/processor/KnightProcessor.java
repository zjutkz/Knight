package com.zjutkz.processor;


import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.zjutkz.AbsKnightDress;
import com.zjutkz.Constants.Config;
import com.zjutkz.annotation.Dress;
import com.zjutkz.annotation.Knight;
import com.zjutkz.annotation.Plug;
import com.zjutkz.info.ComponentInfo;
import com.zjutkz.info.KnightInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by kangzhe on 16/4/2.
 */
@SupportedAnnotationTypes({"com.zjutkz.annotation.Knight","com.zjutkz.annotation.Dress","com.zjutkz.annotation.Plug"})

public class KnightProcessor extends AbstractProcessor {

    private static int justOneTime = 1;

    Elements elementUtils;

    String className = "";

    String packageName = "";

    private Map<String,KnightInfo> mInfoMap = new HashMap<>();

    private Map<String,Map<String,String>> mCustomViewMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);

        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(justOneTime++ > 1){
            return false;
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(Dress.class)){
            if(element.getKind() == ElementKind.FIELD) {
                VariableElement varElement = (VariableElement) element;

                String parentClassName = varElement.getEnclosingElement().getSimpleName().toString();

                String fieldName = varElement.getSimpleName().toString();
                String className = varElement.asType().toString();

                if(mCustomViewMap.containsKey(parentClassName)){
                    mCustomViewMap.get(parentClassName).put(className,fieldName);
                }else{
                    Map<String,String> elements = new HashMap<>();
                    elements.put(className,fieldName);
                    mCustomViewMap.put(parentClassName,elements);
                }
            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(Knight.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                VariableElement varElement = (VariableElement) element;

                getClassAndPackageName(element);

                String resNames[] = varElement.getAnnotation(Knight.class).resName().split(",");
                int[] nightResIds = varElement.getAnnotation(Knight.class).nightResId();
                int[] dayResIds = varElement.getAnnotation(Knight.class).dayResId();

                if(resNames.length != nightResIds.length || resNames.length != dayResIds.length){
                    throw new RuntimeException("The numbers of arguments you set are not consistence!");
                }

                String fieldName = varElement.getSimpleName().toString();
                String fieldType = varElement.asType().toString();

                ComponentInfo componentInfo = new ComponentInfo(fieldName,fieldType);
                for(int i = 0;i < resNames.length;i++){
                    String resName = resNames[i];
                    int nightResId = nightResIds[i];
                    int dayResId = dayResIds[i];
                    componentInfo.setRes(resName,nightResId,dayResId);
                }

                KnightInfo info = mInfoMap.get(className);
                if(info == null){
                    info = new KnightInfo(packageName,className);
                    mInfoMap.put(className, info);
                }
                mInfoMap.get(className).setComponent(componentInfo);
            }else if(element.getKind() == ElementKind.METHOD){
                ExecutableElement executableElement = (ExecutableElement)element;

                int nightRes = executableElement.getAnnotation(Knight.class).nightResId()[0];
                int dayRes = executableElement.getAnnotation(Knight.class).dayResId()[0];

                String methodName = executableElement.getSimpleName().toString();

                getClassAndPackageName(element);

                for(String className : mCustomViewMap.keySet()){
                    TypeElement typeElement = (TypeElement)executableElement.getEnclosingElement();

                    String fieldType = typeElement.asType().toString();

                    ComponentInfo componentInfo = new ComponentInfo(mCustomViewMap.get(className).get(fieldType),fieldType);
                    componentInfo.setRes(methodName,nightRes,dayRes);

                    if(mCustomViewMap.get(className).keySet().contains(fieldType)){
                        KnightInfo info = mInfoMap.get(className);
                        if(info == null ){
                            info = new KnightInfo(packageName,className);
                            mInfoMap.put(className, info);
                        }
                        mInfoMap.get(className).setComponent(componentInfo);
                    }
                }
            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(Plug.class)){
            if (element.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement)element;

                getClassAndPackageName(element);

                String[] resNames = variableElement.getAnnotation(Plug.class).resName().split(",");
                String[] nightResNames = variableElement.getAnnotation(Plug.class).nightResName();
                String[] dayResNames = variableElement.getAnnotation(Plug.class).dayResName();
                String[] valueNames = variableElement.getAnnotation(Plug.class).valueName();
                String packageName = variableElement.getAnnotation(Plug.class).packageName();

                if(resNames.length != nightResNames.length || resNames.length != dayResNames.length || resNames.length != valueNames.length){
                    throw new RuntimeException("The numbers of arguments you set are not consistence!");
                }

                String fieldName = variableElement.getSimpleName().toString();
                String fieldType = variableElement.asType().toString();

                ComponentInfo componentInfo = new ComponentInfo(fieldName,fieldType);
                for(int i = 0;i < resNames.length;i++){
                    String resName = resNames[i];
                    String nightResName = nightResNames[i];
                    String dayResName = dayResNames[i];
                    String valueName = valueNames[i];
                    componentInfo.setPlugRes(resName, nightResName, dayResName,valueName,packageName);
                }

                KnightInfo info = mInfoMap.get(className);
                if(info == null){
                    info = new KnightInfo(packageName,className);
                    mInfoMap.put(className, info);
                }
                mInfoMap.get(className).setComponent(componentInfo);
            }
        }

        for(String clzName : mInfoMap.keySet()){

            KnightInfo info = mInfoMap.get(clzName);


            List<FieldSpec> fieldList = new ArrayList<>();

            for(ComponentInfo componentInfo : info.getComponentInfoList()){
                try {
                    Class componentClz = convertToClass(componentInfo.getType());
                    FieldSpec componentSpec = FieldSpec.builder(componentClz, componentInfo.getName())
                            .addModifiers(Modifier.PRIVATE)
                            .build();
                    fieldList.add(componentSpec);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            String changeToNightMethod = generateChangeToNightMethod(info);

            MethodSpec changeToNight = MethodSpec.methodBuilder("changeToNight")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement(changeToNightMethod)
                    .build();

            String changeToDayMethod = generateChangeToDayMethod(info);

            MethodSpec changeToDay = MethodSpec.methodBuilder("changeToDay")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement(changeToDayMethod)
                    .build();

            String changeToPlugNightMethod = generateChangeToPlugNightMethod(info);

            MethodSpec plugChangeToNight = MethodSpec.methodBuilder("plugChangeToNight")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement(changeToPlugNightMethod)
                    .build();

            String changeToPlugDayMethod = generateChangeToPlugDayMethod(info);
            MethodSpec plugChangeToDay = MethodSpec.methodBuilder("plugChangeToDay")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement(changeToPlugDayMethod)
                    .build();
            try {
                TypeSpec.Builder builder = TypeSpec.classBuilder(mInfoMap.get(clzName).getKnightClassName())
                        .superclass(AbsKnightDress.class)
                        .addModifiers(Modifier.PUBLIC);
                for(FieldSpec spec : fieldList){
                    builder.addField(spec);
                }

                builder.addMethod(changeToNight).addMethod(changeToDay).addMethod(plugChangeToNight).addMethod(plugChangeToDay);

                TypeSpec knightClz = builder.build();

                JavaFile javaFile = JavaFile.builder(mInfoMap.get(clzName).getPackageName(), knightClz)
                        .build();

                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private String generateChangeToPlugDayMethod(KnightInfo knightInfo) {
        int idCount = 0;

        String method = "";

        String contextClassName = knightInfo.getClassName();
        String newClassName = "";
        if(contextClassName.contains(Config.AdapterPlaceHolder)){
            newClassName = contextClassName.replaceAll(Config.AdapterPlaceHolder,".");
        }

        List<ComponentInfo> componentInfoList = knightInfo.getComponentInfoList();

        for(ComponentInfo info : componentInfoList){
            String name = info.getName();
            Map<String,String[]> resMap = info.getPlugResMap();

            for(String resName : resMap.keySet()){
                String dayRes = resMap.get(resName)[1];
                String valueName = resMap.get(resName)[2];
                String packageName = resMap.get(resName)[3];

                if(newClassName.equals("")){
                    switch(resName){
                        case "background":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setBackground(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "src":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setImageDrawable(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "textColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setTextColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "backgroundColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setBackgroundColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        default:
                            method += "((" + contextClassName + ")context)." + name + "." + resName + "(" + dayRes + ");\n";
                            break;
                    }
                }else{
                    switch(resName){
                        case "background":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setBackground(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "src":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setImageDrawable(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "textColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setTextColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "backgroundColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + dayRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setBackgroundColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        default:
                            method += "((" + newClassName + ")inject).context." + name + "." + resName + "(" + dayRes + ");\n";
                            break;
                    }
                }
            }
        }

        return method;
    }

    private String generateChangeToPlugNightMethod(KnightInfo knightInfo) {
        int idCount = 0;

        String method = "";

        String contextClassName = knightInfo.getClassName();
        String newClassName = "";
        if(contextClassName.contains(Config.AdapterPlaceHolder)){
            newClassName = contextClassName.replaceAll(Config.AdapterPlaceHolder,".");
        }

        List<ComponentInfo> componentInfoList = knightInfo.getComponentInfoList();

        for(ComponentInfo info : componentInfoList){
            String name = info.getName();
            Map<String,String[]> resMap = info.getPlugResMap();

            for(String resName : resMap.keySet()){
                String nightRes = resMap.get(resName)[0];
                String valueName = resMap.get(resName)[2];
                String packageName = resMap.get(resName)[3];

                if(newClassName.equals("")){
                    switch(resName){
                        case "background":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setBackground(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "src":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setImageDrawable(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "textColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setTextColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "backgroundColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + contextClassName + ")context)." + name + ".setBackgroundColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        default:
                            method += "((" + contextClassName + ")context)." + name + "." + resName + "(" + nightRes + ");\n";
                            break;
                    }
                }else{
                    switch(resName){
                        case "background":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setBackground(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "src":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setImageDrawable(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "textColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setTextColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        case "backgroundColor":
                            method += "int id" + (++idCount) + " = " + "mResource.getIdentifier(" + "\"" + nightRes + "\"" +
                                    "," + "\"" + valueName + "\"" + "," + "\"" + packageName + "\"" + ");\n";
                            method += "((" + newClassName + ")inject).context." + name + ".setBackgroundColor(mResource.getColor(id" + idCount + "));\n";
                            break;
                        default:
                            method += "((" + newClassName + ")inject).context." + name + "." + resName + "(" + nightRes + ");\n";
                            break;
                    }
                }

            }
        }

        return method;
    }

    private String generateChangeToDayMethod(KnightInfo knightInfo) {
        String method = "";

        String contextClassName = knightInfo.getClassName();
        String newClassName = "";
        if(contextClassName.contains(Config.AdapterPlaceHolder)){
            newClassName = contextClassName.replaceAll(Config.AdapterPlaceHolder,".");
        }

        List<ComponentInfo> componentInfoList = knightInfo.getComponentInfoList();

        for(ComponentInfo info : componentInfoList){
            String name = info.getName();
            Map<String,int[]> resMap = info.getResMap();

            for(String resName : resMap.keySet()){
                int dayRes = resMap.get(resName)[1];
                if(newClassName.equals("")){
                    switch(resName){
                        case "background":
                            method += "((" + contextClassName + ")context)." + name + ".setBackground(((" + contextClassName + ")" + "context).getResources().getDrawable(" + dayRes + "));\n";
                            break;
                        case "src":
                            method += "((" + contextClassName + ")context)." + name + ".setImageDrawable(((" + contextClassName + ")" + "context).getResources().getDrawable(" + dayRes + "));\n";
                            break;
                        case "textColor":
                            method += "((" + contextClassName + ")context)." + name + ".setTextColor(((" + contextClassName + ")" + "context).getResources().getColor(" + dayRes + "));\n";
                            break;
                        case "backgroundColor":
                            method += "((" + contextClassName + ")context)." + name + ".setBackgroundColor(((" + contextClassName + ")" + "context).getResources().getColor(" + dayRes + "));\n";
                            break;
                        default:
                            method += "((" + contextClassName + ")context)." + name + "." + resName + "(" + dayRes + ");\n";
                            break;
                    }
                }else {
                    switch(resName){
                        case "background":
                            method += "((" + newClassName + ")inject)." + name + ".setBackground(((" + newClassName + ")" + "inject).context.getResources().getDrawable(" + dayRes + "));\n";
                            break;
                        case "src":
                            method += "((" + newClassName + ")inject)." + name + ".setImageDrawable(((" + newClassName + ")" + "inject).context.getResources().getDrawable(" + dayRes + "));\n";
                            break;
                        case "textColor":
                            method += "((" + newClassName + ")inject)." + name + ".setTextColor(((" + newClassName + ")" + "inject).context.getResources().getColor(" + dayRes + "));\n";
                            break;
                        case "backgroundColor":
                            method += "((" + newClassName + ")inject)." + name + ".setBackgroundColor(((" + newClassName + ")" + "inject).context.getResources().getColor(" + dayRes + "));\n";
                            break;
                        default:
                            method += "((" + newClassName + ")inject)." + name + "." + resName + "(" + dayRes + ");\n";
                            break;
                    }
                }
            }
        }

        return method;
    }

    private String generateChangeToNightMethod(KnightInfo knightInfo) {
        String method = "";

        String contextClassName = knightInfo.getClassName();
        String newClassName = "";
        if(contextClassName.contains(Config.AdapterPlaceHolder)){
            newClassName = contextClassName.replaceAll(Config.AdapterPlaceHolder,".");
        }

        List<ComponentInfo> componentInfoList = knightInfo.getComponentInfoList();

        for(ComponentInfo info : componentInfoList){
            String name = info.getName();
            Map<String,int[]> resMap = info.getResMap();

            for(String resName : resMap.keySet()){
                int nightRes = resMap.get(resName)[0];

                if(newClassName.equals("")){
                    switch(resName){
                        case "background":
                            method += "((" + contextClassName + ")context)." + name + ".setBackground(((" + contextClassName + ")" + "context).getResources().getDrawable(" + nightRes + "));\n";
                            break;
                        case "src":
                            method += "((" + contextClassName + ")context)." + name + ".setImageDrawable(((" + contextClassName + ")" + "context).getResources().getDrawable(" + nightRes + "));\n";
                            break;
                        case "textColor":
                            method += "((" + contextClassName + ")context)." + name + ".setTextColor(((" + contextClassName + ")" + "context).getResources().getColor(" + nightRes + "));\n";
                            break;
                        case "backgroundColor":
                            method += "((" + contextClassName + ")context)." + name + ".setBackgroundColor(((" + contextClassName + ")" + "context).getResources().getColor(" + nightRes + "));\n";
                            break;
                        default:
                            method += "((" + contextClassName + ")context)." + name + "." + resName + "(" + nightRes + ");\n";
                            break;
                    }
                }else{
                    switch(resName){
                        case "background":
                            method += "((" + newClassName + ")inject)." + name + ".setBackground(((" + newClassName + ")" + "inject).context.getResources().getDrawable(" + nightRes + "));\n";
                            break;
                        case "src":
                            method += "((" + newClassName + ")inject)." + name + ".setImageDrawable(((" + newClassName + ")" + "inject).context.getResources().getDrawable(" + nightRes + "));\n";
                            break;
                        case "textColor":
                            method += "((" + newClassName + ")inject)." + name + ".setTextColor(((" + newClassName + ")" + "inject).context.getResources().getColor(" + nightRes + "));\n";
                            break;
                        case "backgroundColor":
                            method += "((" + newClassName + ")inject)." + name + ".setBackgroundColor(((" + newClassName + ")" + "inject).context.getResources().getColor(" + nightRes + "));\n";
                            break;
                        default:
                            method += "((" + newClassName + ")inject)." + name + "." + resName + "(" + nightRes + ");\n";
                            break;
                    }
                }
            }
        }

        return method;
    }

    private void getClassAndPackageName(Element element) {
        TypeElement classElement = (TypeElement) element.getEnclosingElement();

        PackageElement packageElement = elementUtils.getPackageOf(classElement);

        packageName = packageElement.getQualifiedName().toString();

        if(classElement.getSimpleName().toString().contains("ViewHolder")){
            TypeElement adapterElement = (TypeElement)classElement.getEnclosingElement();
            className = adapterElement.getSimpleName().toString() + Config.AdapterPlaceHolder + classElement.getSimpleName().toString();
        }else{
            className = classElement.getSimpleName().toString();
        }
    }

    private Class convertToClass(String clzName) throws ClassNotFoundException {
        return Class.forName(clzName);
    }
}
