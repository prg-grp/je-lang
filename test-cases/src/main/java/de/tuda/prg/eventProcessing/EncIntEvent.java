package de.tuda.prg.eventProcessing;

public class EncIntEvent extends EncEvent {
    private EncInt val;
    private String source;
    private String origin;

    public EncIntEvent(EncInt val, String source, String origin) {
        this.val = val;
        this.source = source;
        this.origin = origin;
    }

    public EncInt getVal() {
        return val;
    }

    public void setVal(EncInt val) {
        this.val = val;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
