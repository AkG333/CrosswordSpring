package crossword;

import java.util.*;

public class CrosswordSolver {
    public static final int SIZE_W = 5;
    public static final int SIZE_H = 5;
    public static final int MIN_FREQ_W = 20000;
    public static final int MIN_FREQ_H = 20000;
    public static final boolean UNIQUE = true;
    public static final boolean DIAGONALS = false;

    private static final int VTRIE_SIZE = DIAGONALS ? SIZE_W + 2 : SIZE_W;

    private final char[] grid = new char[SIZE_H * SIZE_W];
    private final Trie horizontalTrie;
    private final Trie verticalTrie;
    private final Set<String> banned = new HashSet<>();

    public CrosswordSolver(Trie hTrie, Trie vTrie) {
        this.horizontalTrie = hTrie;
        this.verticalTrie = vTrie;
    }

    public void solve() {
        Trie[] vTries = new Trie[VTRIE_SIZE];
        Arrays.fill(vTries, verticalTrie);
        boxSearch(horizontalTrie, vTries, 0);
    }

    private void boxSearch(Trie trie, Trie[] vTries, int pos) {
        int v_ix = pos % SIZE_W;

        if (v_ix == 0) {
            if (pos == SIZE_H * SIZE_W) {
                printGrid();
                return;
            }
            trie = horizontalTrie;
        }

        Trie.Iter iter = trie.iter();
        while (iter.next()) {
            if (!vTries[v_ix].hasIx(iter.getIx())) continue;
            grid[pos] = iter.getLetter();

            Trie backupV = vTries[v_ix];
            vTries[v_ix] = vTries[v_ix].decend(iter.getIx());

            boxSearch(iter.get(), vTries, pos + 1);
            vTries[v_ix] = backupV;
        }
    }

    private void printGrid() {
        if (UNIQUE && SIZE_W == SIZE_H) {
            for (int i = 0; i < SIZE_H; i++) {
                int same = 0;
                for (int j = 0; j < SIZE_W; j++) {
                    if (grid[i * SIZE_W + j] == grid[j * SIZE_W + i]) {
                        same++;
                    }
                }
                if (same == SIZE_W) return;
            }
        }
        for (int i = 0; i < SIZE_H; i++) {
            for (int j = 0; j < SIZE_W; j++) {
                System.out.print(grid[i * SIZE_W + j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
