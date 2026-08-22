package me.imbanana.itemframeanywhere.util;

public interface IPlayer {
    default boolean isGridSnapping() {
        return false;
    }

    default void setGridSnap(boolean value) {

    }
}
