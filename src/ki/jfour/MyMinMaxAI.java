package ki.jfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MyMinMaxAI extends AI{
    @Override
    public void start(Board b) {
        if(b.getWinner().equals(Player.NONE)){
            Player[][] current = b.getState();
            int height = current.length;
            int width = current[0].length;
            Player MiniMax = b.getCurrentPlayer();
            for (int i = 0; i < 100; i++) {
                minimax(b, i, -100000, 100000, true, height, width, MiniMax);
            }
        }
    }
    @Override
    public String getDescription() {
        return "Dominik Petermann";
    }

    public int minimax(Board b, int depth, int alpha, int beta, boolean max, int height, int width, Player MiniMax){

        Player[][] Board = b.getState();
        //Player current = b.getCurrentPlayer();
        List<Move> possibleMoves = b.possibleMoves();

        int counter = 0;
        while(Board[0][counter]!= Player.NONE /* && !possibleMoves.isEmpty()*/){
            counter++;
            setBestMove(new Move(counter));
        }

        if(depth == 0){
            int heuristicEval = heuristicEval(Board, Player.RED, width, height) - heuristicEval(Board, Player.BLUE, width, height);
            //System.out.println(heuristicEval);
            return heuristicEval;
        }
        if (max){
            int maxEval = -1000000;
            for (Move i:possibleMoves) {
                //System.out.println("Spalte: " + i.column);
                Board boardIter = b;
                if(depth == 2) {
                    System.out.println("*********Spalte: " + i.column + ", Player: " + boardIter.getCurrentPlayer() + " ***********");
                }
                boardIter = boardIter.executeMove(i);
                int eval = minimax(boardIter, depth - 1, alpha, beta, false, height, width, MiniMax);
                System.out.println("Spalte: " + i.column + " Eval: " + eval);
                if (maxEval < eval) {
                    System.out.println("Best Move MAX Spalte: " + i.column + " Eval: " + eval);
                    maxEval = eval;
                    if(MiniMax.equals(Player.RED)) {
                        setBestMove(i);
                    }
                }
                if (alpha < eval){
                    alpha = eval;
                    //System.out.println("Alpha: " + alpha);
                }
                if(alpha >= beta){
                    //System.out.println("PRUUUUUUUUUUUNEEEEEEEEEEEEEEEEEEEEEED");
                    break;
                }
            }
            return maxEval;
        }
        else{
            int minEval = 1000000;
            for (Move i:possibleMoves) {
                //System.out.println("Spalte: " + i.column);
                Board boardIter = b;
                boardIter = boardIter.executeMove(i);
                int eval = minimax(boardIter, depth-1, alpha, beta, true, height, width, MiniMax);
                System.out.println("Spalte: " + i.column + " Eval: " + eval);
                if(minEval > eval) {
                    System.out.println("Best Move MIN Spalte: " + i.column + " Eval: " + eval);
                    minEval = eval;
                    if(MiniMax.equals(Player.BLUE)) {
                        setBestMove(i);
                    }
                }
                if (beta > eval){
                    beta = eval;
                    //System.out.println("Beta: " + beta);
                }
                if(alpha >= beta){
                    //System.out.println("PRUUUUUUUUUUUNEEEEEEEEEEEEEEEEEEEEEED");
                    break;
                }
            }
            return minEval;
        }
    }
    
    public int heuristicEval(Player[][] Board, Player evalPlayer, int width, int height){
        int heuristicEval = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int count_X = 0;
                //Vertikale
                while (j < width && Board[i][j].equals(evalPlayer)){
                    //Unten
                    int count_Y = 1;
                    while ((i+count_Y)<height && Board[i+count_Y][j].equals(evalPlayer)){
                        count_Y++;
                    }
                    if(count_Y > 1 ){
                        heuristicEval++;
                        if(count_Y == 3){heuristicEval = heuristicEval+4;}
                        if(count_Y == 4){heuristicEval = heuristicEval+1000;}
                    }
                    //Diagonale Unten
                    int count_Diag_Unten = 1;
                    while ((i+count_Diag_Unten)<height && (j+count_Diag_Unten)<width && Board[i+count_Diag_Unten][j+count_Diag_Unten].equals(evalPlayer)){
                        count_Diag_Unten++;
                    }
                    if(count_Diag_Unten > 1 ){
                        heuristicEval++;
                        if(count_Diag_Unten == 3){heuristicEval = heuristicEval+4;}
                        if(count_Diag_Unten == 4){heuristicEval = heuristicEval+1000;}
                    }
                    //Diagonale Oben
                    int count_Diag_Oben = 1;
                    while ((i-count_Diag_Oben)>-1 && (j+count_Diag_Oben)<width && Board[i-count_Diag_Oben][j+count_Diag_Oben].equals(evalPlayer)){
                        count_Diag_Oben++;
                    }
                    if(count_Diag_Oben > 1 ){
                        heuristicEval++;
                        if(count_Diag_Oben == 3){heuristicEval = heuristicEval+4;}
                        if(count_Diag_Oben == 4){heuristicEval = heuristicEval+1000;}
                    }
                    count_X++;
                    j++;
                }
                if(count_X > 1 ){//&& Board[i][j].equals(Player.NONE)){
                    heuristicEval++;
                    if(count_X == 3){heuristicEval = heuristicEval+4;}
                    if(count_X == 4){heuristicEval = heuristicEval+1000;}
                }
            }
        }
        return heuristicEval;
    }

    public static void main(String[] args) throws IOException {
        MyMinMaxAI ai = new MyMinMaxAI();
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
