package net.pladema.sgx.backoffice.rest;

import java.util.ArrayList;
import java.util.List;

public class ItemsAllowed <I>{

    public final boolean all;

    public final List<I> ids;

    public ItemsAllowed(boolean all, List<I> ids) {
        this.all = all;
        this.ids = ids;
    }

    public ItemsAllowed() {
        this.all = true;
        this.ids = new ArrayList<>();
    }

    public boolean isEmpty(){
        return ids.isEmpty();
    }
}
