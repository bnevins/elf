/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import static java.awt.event.InputEvent.*;

/**
 *
 * @author bnevins
 */
public class KeyCommand implements Comparable<KeyCommand> {

    private String type;
    private boolean ctrl;
    private boolean shift;
    private boolean alt;
    private String keyDisplay;
    private int keyCode;
    private String relativeTo;
    private String target;
    private final static char USER_PREFS_DELIMITER = ':';

    public RelativeToChoices getRelativeTo() {
        return RelativeToChoices.fromString(relativeTo);
    }
    
    public FileOperationTypes getType() {
        return FileOperationTypes.fromString(type);
    }

    public int getMods() {
        int mods = 0;
        if (ctrl)
            mods += CTRL_DOWN_MASK;
        if (shift)
            mods += SHIFT_DOWN_MASK;
        if (alt)
            mods += ALT_DOWN_MASK;

        return mods;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public String getTarget() {
        return target;
    }

    public KeyCommand(String userPrefsString) {
        String[] ss = userPrefsString.split(":");

        if (ss.length != 7)
            throw new IllegalArgumentException("Bad User Prefs Key Command String: " + userPrefsString);

        type = ss[0];
        ctrl = Boolean.valueOf(ss[1]);
        shift = Boolean.valueOf(ss[2]);
        alt = Boolean.valueOf(ss[3]);
        keyDisplay = ss[4];
        keyCode = Key.getKeyCode(keyDisplay);
        relativeTo = ss[5];
        target = ss[6];
    }

    public KeyCommand(String type, boolean ctrl, boolean shift, boolean alt, String keyDisplay, String relativeTo, String target) {
        this.type = type;
        this.ctrl = ctrl;
        this.shift = shift;
        this.alt = alt;
        this.keyDisplay = keyDisplay;
        this.keyCode = Key.getKeyCode(keyDisplay);
        this.relativeTo = relativeTo;
        this.target = target;
    }

    public Object getColumn(int col) {
        return switch (col) {
            case 0:
                yield type;
            case 1:
                yield ctrl;
            case 2:
                yield shift;
            case 3:
                yield alt;
            case 4:
                yield keyDisplay;
            case 5:
                yield relativeTo;
            case 6:
                yield target;
            default:
                throw new IllegalStateException("Illegal column number: " + col);
        };
    }

    public void setColumn(int col, Object value) {
        switch (col) {
            case 0 ->
                type = (String) value;
            case 1 ->
                ctrl = (Boolean) value;
            case 2 ->
                shift = (Boolean) value;
            case 3 ->
                alt = (Boolean) value;
            case 4 ->
                keyDisplay = value.toString();
            case 5 ->
                relativeTo = (String) value;
            case 6 ->
                target = (String) value;
            default ->
                throw new IllegalStateException("Illegal column number: " + col);
        }
    }

    @Override
    public String toString() {
        //return String.format("%10s", longValues)
        return "    " + type + "  " + ctrl + "  " + shift + "  " + alt + "   " + keyDisplay + "  " + relativeTo + "   " + target;
    }

    @Override
    public boolean equals(Object o) {
        KeyCommand other = (KeyCommand) o;

        if (this == other)
            return true;

        // uniqueness is ONLY based on Ctrl-Shift-Alt-Key combinations!!
        return ctrl == other.ctrl
                && shift == other.shift
                && alt == other.alt
                && keyDisplay.equals(other.keyDisplay);

//             return type.equals(other.type) &&
//                     ctrl == other.ctrl &&
//                     shift == other.shift &&
//                     alt == other.alt &&
//                     key.equals(other.key) &&
//                     relativeTo.equals(other.relativeTo) &&
//                     target.equals(other.target);
    }

    public String createUserPrefsString() {
        // to save to User Preferences
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(USER_PREFS_DELIMITER);
        sb.append(ctrl).append(USER_PREFS_DELIMITER);
        sb.append(shift).append(USER_PREFS_DELIMITER);
        sb.append(alt).append(USER_PREFS_DELIMITER);
        sb.append(keyDisplay).append(USER_PREFS_DELIMITER);
        sb.append(relativeTo).append(USER_PREFS_DELIMITER);
        sb.append(target);
        return sb.toString();
    }

    @Override
    public int compareTo(KeyCommand other) {
        if (equals(other))
            return 0;

        int compare = keyDisplay.compareTo(other.keyDisplay);
        if (compare != 0)
            return compare;

        // ignore all but the Alt-Ctrl-Shift-Key combination
        // keys are the same -- next compare target
//        compare = target.compareTo(other.target);
//        if(compare != 0)
//            return compare;
//        // key and target are the same -- next check relativeTo
//        compare = relativeTo.compareTo(other.relativeTo);
//        if(compare != 0)
//            return compare;
//        // now sort by type...
//        compare = type.compareTo(other.type);
//        if(compare != 0)
//            return compare;
        // Sort the 8 combinations of ctrl-alt-shift!
        int sum = alt ? 1 : 0;
        sum += ctrl ? 2 : 0;
        sum += shift ? 4 : 0;
        int otherSum = other.alt ? 1 : 0;
        otherSum += other.ctrl ? 2 : 0;
        otherSum += other.shift ? 4 : 0;

        if (sum > otherSum)
            return 1;
        else if (sum < otherSum)
            return -1;

        // should NOT happen!
        return 0;
    }

    enum RelativeToChoices {
        ROOT("Root"),
        PARENT("File Parent"),
        GRANDPARENT("File GrandParent"),
        ABSOLUTE("Absolute");

        private final String name;

        RelativeToChoices(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public static RelativeToChoices fromString(String aName) {
            for (RelativeToChoices rtc : RelativeToChoices.values()) {
                if (rtc.name.equalsIgnoreCase(aName)) {
                    return rtc;
                }
            }
            return null;
        }
    }
    // FILE_OPERATION_TYPES[] = {"Copy", "Move", "List", "Index",};
    enum FileOperationTypes {
        COPY("Copy"),
        MOVE("Move"),
        LIST("List"),
        INDEX("Index");

        private final String name;

        FileOperationTypes(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public static FileOperationTypes fromString(String aName) {
            for (FileOperationTypes tc : FileOperationTypes.values()) {
                if (tc.name.equalsIgnoreCase(aName)) {
                    return tc;
                }
            }
            return null;
        }
    }
}
