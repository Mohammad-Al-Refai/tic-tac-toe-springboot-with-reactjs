import { useEffect, useState } from "react";
import useWebSocket from "react-use-websocket";
import { CellIndex, JoinGame, updateGame } from "../service/Request";
import {
  ActionResponse,
  PlayerConnected,
  GameCreated,
  JoinedGame,
  UpdateGame,
  ServerError,
} from "../service/Response";

export function useGameViewModel() {
  //   const WS = "ws://localhost:8080/ws";
  const WS = "ws://192.168.8.101:8080/ws";
  const [isConnected, setIsConnected] = useState(false);
  const [isJoinedGame, setIsJoinedGame] = useState(false);
  const [gameId, setGameId] = useState("");

  const { lastJsonMessage, sendJsonMessage } = useWebSocket(WS, {
    onClose(event) {
      setIsConnected(false);
    },
  });
  const [clientId, setClientId] = useState("");
  const [turn, setTurn] = useState("");
  const [board, setBoard] = useState({
    cell1: "",
    cell2: "",
    cell3: "",
    cell4: "",
    cell5: "",
    cell6: "",
    cell7: "",
    cell8: "",
    cell9: "",
  });
  useEffect(() => {
    if (!lastJsonMessage) {
      return;
    }
    switch (lastJsonMessage.action as ActionResponse) {
      case "CONNECTED":
        onConnected(lastJsonMessage);
        break;
      case "GAME_CREATED":
        onGameCreated(lastJsonMessage);
        break;
      case "JOINED_GAME":
        onJoinedGame(lastJsonMessage);
        break;
      case "UPDATE_GAME":
        onUpdateGame(lastJsonMessage);
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
  }
  function onGameCreated(response: GameCreated) {}
  function onJoinedGame(response: JoinedGame) {
    console.log(response);
    setIsJoinedGame(true);
    setGameId(response.gameId);
  }
  function onUpdateGame(response: UpdateGame) {
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
  }
  function onError(response: ServerError) {
    alert(JSON.stringify(response));
  }
  function onWin(response: ServerError) {
    alert(JSON.stringify(response));
  }
  function onCellClicked(cell: CellIndex) {
    const x = cell[0].toUpperCase() + cell.substring(1, cell.length);
    sendJsonMessage(updateGame(clientId, gameId, x as CellIndex));
  }
  function onJoinClicked() {
    sendJsonMessage(JoinGame(clientId, gameId));
  }
  function onGameIdChange(id: string) {
    setGameId(id);
  }

  return {
    board,
    isConnected,
    onCellClicked,
    clientId,
    isJoinedGame,
    gameId,
    onJoinClicked,
    onGameIdChange,
    turn,
  };
}
