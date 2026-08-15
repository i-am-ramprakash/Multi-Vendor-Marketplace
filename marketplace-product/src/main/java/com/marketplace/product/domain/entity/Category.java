package com.marketplace.product.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Category parent;
    private List<Category> children = new ArrayList<>();
    private Integer displayOrder;
    private Boolean isActive;
    private Integer productCount;
    private Instant createdAt;
    private Instant updatedAt;

    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
        this.displayOrder = 0;
        this.isActive = true;
        this.productCount = 0;
    }

    public Category(String name, String slug, Category parent) {
        this(name, slug);
        this.parent = parent;
    }

    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

    public void incrementProductCount() {
        this.productCount++;
    }

    public void decrementProductCount() {
        if (this.productCount > 0) {
            this.productCount--;
        }
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public int getDepth() {
        int depth = 0;
        Category current = this.parent;
        while (current != null) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }

    public String getFullPath() {
        StringBuilder path = new StringBuilder(name);
        Category current = this.parent;
        while (current != null) {
            path.insert(0, current.getName() + " > ");
            current = current.getParent();
        }
        return path.toString();
    }

    public List<Category> getAncestors() {
        List<Category> ancestors = new ArrayList<>();
        Category current = this.parent;
        while (current != null) {
            ancestors.add(0, current);
            current = current.getParent();
        }
        return ancestors;
    }
}
