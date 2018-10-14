package app.controllers;

import app.models.ClassProperty;
import app.models.MethodProperty;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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

    public ClassAndMethodPropertyExtraction(){
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Void arg) {
        super.visit(classOrInterfaceDeclaration, arg);

        className  = classOrInterfaceDeclaration.getName().toString();
        sourceCode = classOrInterfaceDeclaration.toString();
        lineOfCode = (classOrInterfaceDeclaration.getEnd().get().line - classOrInterfaceDeclaration.getBegin().get().line - 1) ;
        lineOfComment = classOrInterfaceDeclaration.getAllContainedComments().stream().mapToInt(comment -> ((comment.getEnd().get().line - comment.getBegin().get().line) + 1)).sum();

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
                    int methodLineOfCode = (methodDeclaration.getType().toString().equalsIgnoreCase("void")) ? (methodDeclaration.getEnd().get().line - methodDeclaration.getBegin().get().line - 2) : (methodDeclaration.getEnd().get().line - methodDeclaration.getBegin().get().line - 1);
                    int methodLineOfComment = methodDeclaration.getAllContainedComments().stream().mapToInt(comment -> ((comment.getEnd().get().line - comment.getBegin().get().line) + 1)).sum();
                    String methodSourceCode = methodDeclaration.toString();

                    methodProperty.set(className, methodName, methodLineOfCode, methodLineOfComment, methodSourceCode);
                }
            });
        }
    }
}
