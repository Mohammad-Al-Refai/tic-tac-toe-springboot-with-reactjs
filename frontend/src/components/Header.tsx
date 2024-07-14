export function Header({ id }: HeaderProps) {
  return (
    <div className="w-100 flex align-items-center justify-content-space-evenly">
      <div></div>
      <h1 className="animate-title">Tic Tac Toe</h1>
      <p>ID: {id}</p>
    </div>
  );
}

interface HeaderProps {
  id: string;
}
