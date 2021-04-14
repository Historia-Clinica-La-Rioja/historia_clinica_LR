package net.pladema.snowstorm.services.inferredrules;

import lombok.Getter;
import lombok.Setter;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class TreeNode<T, A> {

    private T data;
    private A attributes;
    private TreeNode<T,A> parent;
    private List<TreeNode<T,A>> children = new LinkedList<>();

    public boolean isRoot() {
        return parent == null;
    }


    public TreeNode(T data, A attributes) {
        this.data = data;
        this.attributes = attributes;
    }

    public TreeNode<T,A> addChild(T child, A attributes) {
        TreeNode<T,A> childNode = new TreeNode<>(child, attributes);
        childNode.setParent(this);
        this.children.add(childNode);
        return childNode;
    }

    public A findTreeNode(List<SnowstormItemResponse> ancestors) {
        A inferred = null;
        A inferredChild = null;

        if(ancestors.stream().anyMatch(r -> r.getConceptId().equals(data))){
            inferred = attributes;

            for (TreeNode<T,A> child : getChildren()){
                if(inferredChild == null)
                    inferredChild = child.findTreeNode(ancestors);
            }
        }
        return inferredChild != null ? inferredChild : inferred;
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }
}
