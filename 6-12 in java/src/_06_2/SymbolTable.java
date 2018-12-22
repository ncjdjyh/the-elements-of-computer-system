package _06_2;

import lombok.Data;

import java.util.HashMap;

@Data
public class SymbolTable {
    /**
     * @Auther: ncjdjyh
     * @Date: 2018/11/22 16:27
     * @Description: 符号表
     */
    private static HashMap<String, Integer> ramAddressTable;
    private static SymbolTable instance;
    // 下一个可用 RAM 单元, 从 16 开始 0 - 15 16384 24576 是预置的符号
    private int nextAvailableRAMAddress;
    // 下一个可用 ROM 单元, 从 0 开始
    private int nextAvailableROMAddress;

    private SymbolTable() {
        ramAddressTable = new HashMap<>();
        nextAvailableRAMAddress = 16;
        setupBuiltInTable();
    }

    public static synchronized SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    private void setupBuiltInTable() {
        ramAddressTable.put("SP", 0);
        ramAddressTable.put("LCL", 1);
        ramAddressTable.put("ARG", 2);
        ramAddressTable.put("THIS", 3);
        ramAddressTable.put("THAT", 4);
        for (int i = 0; i <= 15; i++) {
            ramAddressTable.put(String.format("R%s", i), i);
        }
        ramAddressTable.put("SCREEN", 16384);
        ramAddressTable.put("KBD", 24576);
    }

    public static HashMap<String, Integer> getRamAddressTable() {
        return ramAddressTable;
    }

    public void addEntry(String symbol, int address) {
        ramAddressTable.put(symbol, address);
    }

    public Integer getAddress(String symbol) {
        return ramAddressTable.get(symbol);
    }

    public int getNextAvailableRAMAaddress() {
        return nextAvailableRAMAddress;
    }

    public void increaseNextAvailableRAMAddress() {
        nextAvailableRAMAddress += 1;
    }

    public void increaseNextAvailableROMAddress() {
        nextAvailableROMAddress += 1;
    }

    public boolean contains(String symbol) {
        return ramAddressTable.containsKey(symbol);
    }

    public void display() {
        System.out.println("nextAvailableRAMAddress" + nextAvailableRAMAddress);
        System.out.println("nextAvailableROMAddress" + nextAvailableROMAddress);
        for (String key : ramAddressTable.keySet()) {
            System.out.println("key:" + key + "-----" + "value:" + ramAddressTable.get(key));
        }
    }
}