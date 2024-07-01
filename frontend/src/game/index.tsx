import { CellIndex } from "../service/Request";
import { useGameViewModel } from "./gameViewModel";

export function Game() {
  const {
    board,
    onCellClicked,
    isConnected,
    clientId,
    isJoinedGame,
    gameId,
    turn,
    createdGameId,
    opponent,
    onJoinClicked,
    onCreateGameClicked,
    onGameIdChange,
  } = useGameViewModel();
  return (
    <div className="game-container">
      <div className="debug">
        <p>isConnected: {String(isConnected)}</p>
        <p>clientId: {clientId}</p>
        <p>isJoinedGame: {String(isJoinedGame)}</p>
        <p>gameId: {gameId}</p>
        <p>turn: {turn}</p>
        <p>createdGameId: {createdGameId}</p>
        <p>opponent: {JSON.stringify(opponent)}</p>

        <input
          placeholder="gameId"
          onChange={(e) => onGameIdChange(e.target.value)}
        />
        <button onClick={onJoinClicked}>join</button>
        <button onClick={onCreateGameClicked}>create game</button>
      </div>

      <div className="game-board">
        {Object.keys(board).map((cell) => {
          return (
            <div
              className="game-cell"
              key={cell}
              onClick={() => onCellClicked(cell as CellIndex)}
            >
              {board[cell]}
            </div>
          );
        })}
      </div>
    </div>
  );
}
