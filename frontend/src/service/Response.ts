export type ActionResponse =
  | "ERROR"
  | "CONNECTED"
  | "GAME_CREATED"
  | "JOINED_GAME"
  | "UPDATE_GAME"
  | "NEW_PLAYER_JOINED"
  | "PLAYER_QUIET"
  | "WIN";
export type CellState = "NONE" | "X" | "O";

export interface ServerError {
  action: "ERROR";
  errorMessage: string;
}
export interface PlayerConnected {
  action: "CONNECTED";
  clientId: string;
}
export interface GameCreated {
  action: "GAME_CREATED";
  gameId: string;
}
export interface GameWin {
  action: "WIN";
  gameId: string;
  playerId: string;
  winner: CellState;
}
export interface JoinedGame {
  action: "JOINED_GAME";
  gameId: string;
  playerId1: string | null;
  playerId2: string | null;
  turn: CellState;
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

export interface UpdateGame {
  action: "UPDATE_GAME";
  gameId: string;
  playerIdTurn: string;
  cell1: CellState;
  cell2: CellState;
  cell3: CellState;
  cell4: CellState;
  cell5: CellState;
  cell6: CellState;
  cell7: CellState;
  cell8: CellState;
  cell9: CellState;
  turn: string;
}
export interface NewPlayerJoinedGame {
  action: "NEW_PLAYER_JOINED";
  gameId: string;
  playerId: string;
  playerName: string;
}
export interface PlayerQuiet {
  action: "NEW_PLAYER_JOINED";
  gameId: string;
  playerId: string;
  playerName: string;
}
