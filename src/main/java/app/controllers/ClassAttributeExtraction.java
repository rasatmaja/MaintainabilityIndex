package app.controllers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassAttributeExtraction extends VoidVisitorAdapter<Void> {
    int lineOfComment = 0;
    @Override
    public void visit(ClassOrInterfaceDeclaration coid, Void arg) {
        super.visit(coid, arg);

        System.out.println("Class Name  : " + coid.getName());
        //System.out.println("Source Code : \n" + coid.toString());
        System.out.println("line of code     : " + (coid.getEnd().get().line - coid.getBegin().get().line - 1) );

        coid.getAllContainedComments().forEach((comment) -> {
            lineOfComment += ((comment.getEnd().get().line - comment.getBegin().get().line) + 1);
        });

        System.out.println("line of code     : " + lineOfComment);

        coid.walk(node -> {
            if (node instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) node;
                System.out.println("Method Name : " + md.getDeclarationAsString(false, true, false));
            }
        });
        
    }
}
