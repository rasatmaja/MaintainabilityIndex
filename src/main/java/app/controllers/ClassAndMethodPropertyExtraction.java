package app.controllers;

import app.models.ClassEdgeProperty;
import app.models.ClassProperty;
import app.models.MethodProperty;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassAndMethodPropertyExtraction extends VoidVisitorAdapter<Void> {
    int lineOfComment = 0;
    int lineOfCode = 0;
    String sourceCode;
    String className;
    String classType;
    ClassProperty classProperty;
    MethodProperty methodProperty;
    ClassEdgeProperty classEdgeProperty;

    public ClassAndMethodPropertyExtraction(){
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();
        classEdgeProperty = ClassEdgeProperty.getInstance();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Void arg) {
        super.visit(classOrInterfaceDeclaration, arg);

        className  = classOrInterfaceDeclaration.getName().toString();
        sourceCode = classOrInterfaceDeclaration.toString();
        lineOfCode = (classOrInterfaceDeclaration.getEnd().get().line - classOrInterfaceDeclaration.getBegin().get().line - 1) ;
        lineOfComment = classOrInterfaceDeclaration.getAllContainedComments().stream().mapToInt(comment -> ((comment.getEnd().get().line - comment.getBegin().get().line) + 1)).sum();

        classOrInterfaceDeclaration.getExtendedTypes().forEach(extendedType -> {
            classEdgeProperty.set(className, extendedType.getNameAsString() );
        });

        classOrInterfaceDeclaration.getImplementedTypes().forEach(implementedType -> {
            classEdgeProperty.set(className, implementedType.getNameAsString() );
        });

        if (classOrInterfaceDeclaration.isAbstract()){
            classType = "Abstract";
            classProperty.set(className,lineOfCode,lineOfComment,sourceCode, classType);
        } else if (classOrInterfaceDeclaration.isInterface()){
            classType = "Interface";
            classProperty.set(className,lineOfCode,lineOfComment,sourceCode, classType);
        } else{
            classType = "Concrete";
            classProperty.set(className,lineOfCode,lineOfComment,sourceCode, classType);

            classOrInterfaceDeclaration.walk(node -> {
                if (node instanceof MethodDeclaration) {
                    MethodDeclaration methodDeclaration = (MethodDeclaration) node;
                    //System.out.println("Method Name : " + methodDeclaration.getDeclarationAsString(false, true, false));
                    //System.out.println("body Method : \n" + methodDeclaration.getBody().get().toString());
                    String methodName = methodDeclaration.getDeclarationAsString(false, true, false);

                    String simpleName = methodDeclaration.getName().asString();
                    //String type = methodDeclaration.getType().asString();
                    String type = "method";
                    String parameterType = methodDeclaration.getTypeParameters().toString();
                    String parameterName = methodDeclaration.getParameters().toString();

                    int methodLineOfComment = methodDeclaration.getAllContainedComments().stream().mapToInt(comment -> ((comment.getEnd().get().line - comment.getBegin().get().line) + 1)).sum();
                    methodLineOfComment = (methodLineOfComment < 0) ? 0 : methodLineOfComment;

                    String methodSourceCode = methodDeclaration.toString();
                    String bodyMethod = methodDeclaration.getBody().get().toString();

                    bodyMethod.replaceAll("(?m)^\\s", "");
                    int methodLineOfCode = bodyMethod.split(System.getProperty("line.separator")).length - 2;

                    methodProperty.set(className, methodName, methodLineOfCode, methodLineOfComment, methodSourceCode, bodyMethod, simpleName, type, parameterType, parameterName);

                } else if (node instanceof ConstructorDeclaration){
                    ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) node;

                    String methodName = constructorDeclaration.getDeclarationAsString(false, true, false);

                    String simpleName = constructorDeclaration.getName().asString();
                    //String type = className;
                    String type = "constructor";
                    String parameterType = constructorDeclaration.getTypeParameters().toString();
                    String parameterName = constructorDeclaration.getParameters().toString();


                    int methodLineOfComment = constructorDeclaration.getAllContainedComments().stream().mapToInt(comment -> ((comment.getEnd().get().line - comment.getBegin().get().line) + 1)).sum();
                    methodLineOfComment = (methodLineOfComment < 0) ? 0 : methodLineOfComment;

                    String methodSourceCode = constructorDeclaration.toString();
                    String bodyMethod = constructorDeclaration.getBody().toString();
                    bodyMethod.replaceAll("(?m)^\\s", "");
                    int methodLineOfCode = bodyMethod.split(System.getProperty("line.separator")).length - 2;

                    methodSourceCode.replaceAll("(?m)^\\s", "");
                    bodyMethod = "class "+className+" {\n " + className +"()"+ bodyMethod + "\n }";


                    methodProperty.set(className, methodName, methodLineOfCode, methodLineOfComment, methodSourceCode, bodyMethod, simpleName, type, parameterType, parameterName);

                }
            });
        }
    }
}
