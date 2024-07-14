import { useEffect, useState } from "react";
import useWebSocket from "react-use-websocket";
import {
  CellIndex,
  JoinGame,
  createGame,
  getAvailableGames,
  updateGame,
} from "../service/Request";
import {
  ActionResponse,
  PlayerConnected,
  GameCreated,
  JoinedGame,
  UpdateGame,
  ServerError,
  NewPlayerJoinedGame,
  PlayerQuiet,
  CellState,
  AvailableGames,
} from "../service/Response";
export interface IBoard {
  cell1: CellState;
  cell2: CellState;
  cell3: CellState;
  cell4: CellState;
  cell5: CellState;
  cell6: CellState;
  cell7: CellState;
  cell8: CellState;
  cell9: CellState;
}
export function useGameViewModel() {
  const WS = "ws://192.168.8.103:8080/ws";
  const [isConnected, setIsConnected] = useState(false);
  const [isJoinedGame, setIsJoinedGame] = useState(false);
  const [gameId, setGameId] = useState("");
  const [createdGameId, setCreatedGameId] = useState("");
  const [opponent, setOpponent] = useState({ name: "", id: "" });
  const [iam, setIam] = useState<CellState>("NONE");
  const [availableGames, setAvailableGames] = useState<string[]>([]);
  const { lastJsonMessage, sendJsonMessage } = useWebSocket(WS, {
    reconnectAttempts: 3,
    onClose(event) {
      setIsConnected(false);
    },
  });
  const [clientId, setClientId] = useState("");
  const [turn, setTurn] = useState<CellState>("NONE");
  const [board, setBoard] = useState<IBoard>({
    cell1: "NONE",
    cell2: "NONE",
    cell3: "NONE",
    cell4: "NONE",
    cell5: "NONE",
    cell6: "NONE",
    cell7: "NONE",
    cell8: "NONE",
    cell9: "NONE",
  });
  useEffect(() => {
    if (!lastJsonMessage) {
      return;
    }
    switch (lastJsonMessage.action as ActionResponse) {
      case "CONNECTED":
        onConnected(lastJsonMessage);
        console.log(clientId);

        break;
      case "GAME_CREATED":
        onGameCreated(lastJsonMessage);
        break;
      case "AVAILABLE_GAMES":
        onRetrieveAvailableGames(lastJsonMessage);
        break;
      case "JOINED_GAME":
        onJoinedGame(lastJsonMessage);
        break;
      case "UPDATE_GAME":
        onUpdateGame(lastJsonMessage);
        break;
      case "NEW_PLAYER_JOINED":
        onNewPlayerJoinedGame(lastJsonMessage);
        break;
      case "PLAYER_QUIET":
        onOpponentQuiet(lastJsonMessage);
        break;
      case "WIN":
        onWin(lastJsonMessage);
        break;
      case "ERROR":
        onError(lastJsonMessage);
        break;
      default:
        break;
    }
  }, [lastJsonMessage]);

  function onConnected(response: PlayerConnected) {
    setIsConnected(true);
    setClientId(response.clientId);
    refreshAvailableGames(response.clientId);
  }
  function onGameCreated(response: GameCreated) {
    setCreatedGameId(response.gameId);
  }
  function onOpponentQuiet(response: PlayerQuiet) {
    alert(JSON.stringify(response));
  }
  function onNewPlayerJoinedGame(response: NewPlayerJoinedGame) {
    setOpponent({
      name: response.playerName,
      id: response.playerId,
    });
    alert(JSON.stringify(response));
  }
  function onJoinedGame(response: JoinedGame) {
    setTurn(response.turn);
    setBoard({
      cell1: response.cell1,
      cell2: response.cell2,
      cell3: response.cell3,
      cell4: response.cell4,
      cell5: response.cell5,
      cell6: response.cell6,
      cell7: response.cell7,
      cell8: response.cell8,
      cell9: response.cell9,
    });
    setIsJoinedGame(true);
    if (response.playerId1 == clientId) {
      alert("X");
      setIam("X");
    }
    if (response.playerId2 == clientId) {
      alert("O");
      setIam("O");
    }
    setGameId(response.gameId);
  }
  function refreshAvailableGames(id: string) {
    sendJsonMessage(getAvailableGames(id));
  }
  function onUpdateGame(response: UpdateGame) {
    setTurn(response.turn as CellState);
    setBoard({
      cell1: response.cell1,
      cell2: response.cell2,
      cell3: response.cell3,
      cell4: response.cell4,
      cell5: response.cell5,
      cell6: response.cell6,
      cell7: response.cell7,
      cell8: response.cell8,
      cell9: response.cell9,
    });
  }
  function onError(response: ServerError) {
    alert(JSON.stringify(response));
  }
  function onWin(response: ServerError) {
    alert(JSON.stringify(response));
  }
  function onCellClicked(cell: CellIndex) {
    const cellIndex = cell[0].toUpperCase() + cell.substring(1, cell.length);
    sendJsonMessage(updateGame(clientId, gameId, cellIndex as CellIndex));
  }
  function onJoinClicked(gameId: string) {
    sendJsonMessage(JoinGame(clientId, gameId));
  }
  function onGameIdChange(id: string) {
    setGameId(id);
  }
  function onCreateGameClicked() {
    sendJsonMessage(createGame(clientId));
  }
  function onRetrieveAvailableGames(response: AvailableGames) {
    setAvailableGames(response.ids);
  }

  return {
    board,
    isConnected,
    onCellClicked,
    clientId,
    isJoinedGame,
    gameId,
    createdGameId,
    opponent,
    onJoinClicked,
    onGameIdChange,
    onCreateGameClicked,
    turn,
    iam,
    availableGames,
    refreshAvailableGames,
  };
}
