export enum ActionRequest {
  CREATE_GAME,
  JOIN_GAME,
  UPDATE_GAME,
  GET_AVAILABLE_GAMES,
}
export type CellIndex =
  | "Cell1"
  | "Cell2"
  | "Cell3"
  | "Cell4"
  | "Cell5"
  | "Cell6"
  | "Cell7"
  | "Cell8"
  | "Cell9";

function ActionRequestToString(action: ActionRequest) {
  switch (action) {
    case ActionRequest.CREATE_GAME:
      return "CREATE_GAME";
    case ActionRequest.JOIN_GAME:
      return "JOIN_GAME";
    case ActionRequest.UPDATE_GAME:
      return "UPDATE_GAME";
    case ActionRequest.GET_AVAILABLE_GAMES:
      return "GET_AVAILABLE_GAMES";
    default:
      return "";
  }
}
export function createGame(clientId: string) {
  return {
    action: ActionRequestToString(ActionRequest.CREATE_GAME),
    clientId,
  };
}
export function JoinGame(clientId: string, gameId: string) {
  return {
    action: ActionRequestToString(ActionRequest.JOIN_GAME),
    clientId,
    gameId,
  };
}
export function updateGame(
  clientId: string,
  gameId: string,
  cellIndex: CellIndex
) {
  return {
    action: ActionRequestToString(ActionRequest.UPDATE_GAME),
    clientId,
    gameId,
    cellIndex,
  };
}
export function getAvailableGames(clientId: string) {
  return {
    action: ActionRequestToString(ActionRequest.GET_AVAILABLE_GAMES),
    clientId,
  };
}
