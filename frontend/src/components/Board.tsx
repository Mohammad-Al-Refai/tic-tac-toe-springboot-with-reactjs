import { IBoard } from "../game/gameViewModel";
import { CellIndex } from "../service/Request";
import { CellState } from "../service/Response";
import X from "../assets/x.svg?react";
import O from "../assets/o.svg?react";
export function Board({ board, onCellClicked, whoamI }: BoardProps) {
  function renderCell(cell: CellState) {
    if (cell == "NONE") {
      return null;
    }
    if (cell == "X") {
      return <X />;
    }
    if (cell == "O") {
      return <O />;
    }
  }
  function getClassName(cell: CellState) {
    let className = "cell";
    if (whoamI == "X" && cell == "X") {
      className += " bg-secondary";
      return className;
    }
    if (whoamI == "X" && cell == "O") {
      className += " bg-tertiary";
      return className;
    }
    if (whoamI == "O" && cell == "O") {
      className += " bg-secondary";
      return className;
    }
    if (whoamI == "O" && cell == "X") {
      className += " bg-tertiary";
      return className;
    }
    return className;
  }
  return (
    <div className="board">
      {Object.keys(board).map((cell) => {
        return (
          <div
            className={getClassName(board[cell as keyof IBoard])}
            key={cell}
            onClick={() => onCellClicked(cell as CellIndex)}
          >
            {renderCell(board[cell as keyof IBoard])}
          </div>
        );
      })}
    </div>
  );
}

interface BoardProps {
  board: IBoard;
  onCellClicked: (index: CellIndex) => void;
  whoamI: CellState;
}
