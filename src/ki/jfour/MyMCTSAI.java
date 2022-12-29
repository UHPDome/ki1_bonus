package ki.jfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class MyMCTSAI extends AI{

    @Override
    public void start(Board b) {
        if(b.getWinner().equals(Player.NONE)){
            MCTS root = new MCTS(b, new MCTS(), Collections.emptyList(), 0, 0, new Move(0));
            for (int i = 0; i < 2; i++) {
                Expansion(Selection(root));
                UpdateBestMove(root);
            }
        }
    }

    public class MCTS {
        private Board state;
        private MCTS parent;
        private List<MCTS> children;
        private int win;
        private int played;

        private Move current;

        public MCTS(Board state, MCTS parent, List<MCTS> children, int win, int played, Move current) {
            this.state = state;
            this.parent = parent;
            this.children = children;
            this.win = win;
            this.played = played;
            this.current = current;
        }

        public MCTS() {
        }
    }

    public MCTS Selection(MCTS Select){
        while(!Select.children.equals(Collections.emptyList())){
            double eval = 0.0;

            for (MCTS child:Select.children
                 ) {
                int comp = child.win/child.played;
                if(eval < comp){
                    eval = comp;
                    Select = child;
                }
            }
        }
        return Select;
    }

    public void Expansion(MCTS Expand){
        int wins = 0;
        int played = 0;
        for (Move m:Expand.state.possibleMoves()
             ) {
            Board nextBoard = Expand.state;
            nextBoard.executeMove(m);
            int simulate = Simulate(nextBoard);
            Expand.children.add(new MCTS(nextBoard, Expand, Collections.emptyList(), simulate, 1, m));
            if(simulate == 1){
                wins++;
            }
            played++;
        }
        Update(Expand, wins, played);
    }

    public int Simulate(Board b){
        int win = 0;
        Board simulate = b;
        Random ran = new Random();

        int failcount = 0;
        while(simulate.getWinner().equals(Player.NONE)){
            List<Move> Moves = simulate.possibleMoves();
            if(Moves.size() == 0){return win;}
            int move = ran.nextInt(Moves.size());
            simulate.executeMove(Moves.get(move));
        }

        if(simulate.getWinner().equals(Player.RED)){
            win++;
        }
        return win;
    }

    public void Update(MCTS Update, int win, int played){
        while(!Update.parent.equals(new MCTS())){
            Update.win = Update.win + win;
            Update.played = Update.played + played;
        }
    }
    public void UpdateBestMove(MCTS bestMove){
        int bestPlayed = 0;
        int bestWin = 0;
        for (MCTS child: bestMove.children
             ) {
            if(child.played > bestPlayed){
                bestPlayed = child.played;
                bestWin = child.win;
                setBestMove(child.current);
            }
            else if(child.played == bestPlayed && child.win > bestWin){
                bestWin = child.win;
                setBestMove(child.current);
            }
        }
    }
    @Override
    public String getDescription() {
        return "Dominik Petermann";
    }

    public static void main(String[] args) throws IOException {
        MyMCTSAI ai = new MyMCTSAI();
        Board b = new Board(5, 5);

        for (int i = 0; i < 10; i++) {
            ai.start(b);
            b = b.executeMove(ai.getBestMove().get());
            // /*
            System.out.println(b.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter String");
            String exec = br.readLine();
            int execute = parseInt(exec);
            b = b.executeMove(new Move(execute));
            // */ b = b.executeMove(new Move(0));

        }
    }
}
