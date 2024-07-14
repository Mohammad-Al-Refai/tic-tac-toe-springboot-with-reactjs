import { AvailableGamesList } from "../components/AvailableGamesList";
import { Board } from "../components/Board";
import { CreateGameEntries } from "../components/CreateGameEntries";
import { Header } from "../components/Header";
import { JoinGameEntries } from "../components/JoinGameEntries";
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
    iam,
    availableGames,
    refreshAvailableGames,
  } = useGameViewModel();
  return (
    <div className="flex w-100 col align-items-center p-xl5 ">
      <Header id={clientId} />
      <div className="flex align-items-center justify-content-center w-100 h-100 mt-xl5">
        <Board whoamI={iam} board={board} onCellClicked={onCellClicked} />
      </div>
      <JoinGameEntries onJoinClicked={onJoinClicked} />
      <CreateGameEntries
        onCreateClicked={onCreateGameClicked}
        gameId={createdGameId}
        joinGame={onJoinClicked}
      />
      <AvailableGamesList
        games={availableGames}
        onRefresh={refreshAvailableGames}
      />
    </div>
  );
}
