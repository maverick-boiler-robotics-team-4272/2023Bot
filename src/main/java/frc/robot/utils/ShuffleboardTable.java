package frc.robot.utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

public class ShuffleboardTable {
    private static Map<String, ShuffleboardTable> tableCache = new HashMap<>();

    private Map<String, SimpleWidget> m_keyEntryMap = new HashMap<>();
    private Map<String, ComplexWidget> m_sendableMap = new HashMap<>();
    private ShuffleboardTab m_tab;
    private ShuffleboardTable(String name){
        m_tab = Shuffleboard.getTab(name);
    }

    private SimpleWidget putEntry(String key, Object value){
        if(hasKey(key)){
            m_keyEntryMap.get(key).getEntry().setValue(value);
        }else{
            m_keyEntryMap.put(key, m_tab.add(key, value));
        }

        return m_keyEntryMap.get(key);
    }

    private ComplexWidget putEntry(String key, Sendable sendable){
        if(!hasKey(key)){
            m_sendableMap.put(key, m_tab.add(key, sendable));
            return m_sendableMap.get(key);
        }
        throw new Error(key + " already exists in sendables");
    }

    private GenericEntry getEntry(String key, Object defaultValue){
        if(!hasKey(key)){
            putEntry(key, defaultValue);
        }

        return m_keyEntryMap.get(key).getEntry();
    }

    public boolean hasKey(String key){
        return m_keyEntryMap.containsKey(key) || m_sendableMap.containsKey(key);
    }

    public SimpleWidget putNumber(String key, double value){
        return putEntry(key, value);
    }

    public double getNumber(String key, double defaultValue){
        return getEntry(key, defaultValue).getDouble(defaultValue);
    }

    public double getNumber(String key){
        return getNumber(key, 0.0);
    }

    public SimpleWidget putNumberArray(String key, double[] value) {
        return putEntry(key, value);
    }

    public double[] getNumberArray(String key, double[] defaultValue) {
        return getEntry(key, defaultValue).getDoubleArray(defaultValue);
    }

    public double[] getNumberArray(String key) {
        return getNumberArray(key, new double[0]);
    }

    public SimpleWidget putBoolean(String key, boolean value){
        return putEntry(key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue){
        return getEntry(key, defaultValue).getBoolean(defaultValue);
    }

    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    public SimpleWidget putBooleanArray(String key, boolean[] value) {
        return putEntry(key, value);
    }

    public boolean[] getBooleanArray(String key, boolean[] defaultValue) {
        return getEntry(key, defaultValue).getBooleanArray(defaultValue);
    }

    public boolean[] getBooleanArray(String key) {
        return getBooleanArray(key, new boolean[0]);
    }

    public SimpleWidget putString(String key, String value){
        return putEntry(key, value);
    }

    public String getString(String key, String defaultValue){
        return getEntry(key, defaultValue).getString(defaultValue);
    }

    public String getString(String key){
        return getString(key, "");
    }

    public SimpleWidget putStringArray(String key, String[] value) {
        return putEntry(key, value);
    }

    public String[] getStringArray(String key, String[] defaultValue) {
        return getEntry(key, defaultValue).getStringArray(defaultValue);
    }

    public String[] getStringArray(String key) {
        return getStringArray(key, new String[0]);
    }

    public ComplexWidget putData(String key, Sendable data){
        return putEntry(key, data);
    }

    public SimpleWidget getWidget(String key){
        if(m_keyEntryMap.containsKey(key)){
            return m_keyEntryMap.get(key);
        } else {
            return null;
        }
    }

    public static ShuffleboardTable getTable(String name){
        if(tableCache.containsKey(name)){
            return tableCache.get(name);
        }else{
            ShuffleboardTable tab = new ShuffleboardTable(name);
            tableCache.put(name, tab);
            return tab;
        }
    }
}