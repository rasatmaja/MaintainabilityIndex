package app.controllers;

import app.models.ClassAttribute;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassAttributeExtraction extends VoidVisitorAdapter<Void> {
    int lineOfComment = 0;
    int lineOfCode = 0;
    String sourceCode;
    String className;
    String classType;
    ClassAttribute classAttribute;

    public ClassAttributeExtraction(){
        classAttribute = ClassAttribute.getInstance();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration coid, Void arg) {
        super.visit(coid, arg);

        className  = coid.getName().toString();
        sourceCode = coid.toString();
        lineOfCode = (coid.getEnd().get().line - coid.getBegin().get().line - 1) ;

        coid.getAllContainedComments().forEach((comment) -> {
            lineOfComment += ((comment.getEnd().get().line - comment.getBegin().get().line) + 1);
        });

        if (coid.isAbstract()){
            classType = "Abstract";
            classAttribute.set(className,lineOfCode,lineOfComment,sourceCode, classType);
        } else if (coid.isInterface()){
            classType = "Interface";
            classAttribute.set(className,lineOfCode,lineOfComment,sourceCode, classType);
        } else{
            classType = "Concrete";
            classAttribute.set(className,lineOfCode,lineOfComment,sourceCode, classType);

            coid.walk(node -> {
                if (node instanceof MethodDeclaration) {
                    MethodDeclaration md = (MethodDeclaration) node;
                    System.out.println("Method Name : " + md.getDeclarationAsString(false, true, false));
                    System.out.println("body Method : \n" + md.getBody());
                }
            });
        }




        
    }
}
