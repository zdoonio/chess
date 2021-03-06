package model

/**
  * Created by jAANUSZEK0700 on 02.09.2018.
  */
import GamePieceType._
import utils.InputUtils.readInt

/**
  * Chess piece object
  * @param pieceType  type of chess piece
  */
case class GamePiece (
  pieceType: GamePieceType
) {

  /**
    * Returns all movement options of certain game piece
    * @param row      row number
    * @param column   column number
    * @param game     game object
    */
  def allMovementOptions(row: Int, column: Int, game: Game): List[(Int, Int)] = {
    pieceType match {

      case KING =>
        List((row - 1, column - 1), (row - 1, column), (row - 1, column + 1), (row, column - 1), (row, column + 1),
          (row + 1, column - 1), (row + 1, column), (row + 1, column + 1))

      case QUEEN =>
        bishopMovement(row, column, game) ::: rookMovement(row, column, game)

      case BISHOP =>
        bishopMovement(row, column, game)

      case ROOK =>
        rookMovement(row, column, game)

      case KNIGHT =>
        List((row - 2, column - 1), (row - 2, column + 1), (row - 1, column - 2), (row - 1, column + 2),
          (row + 1, column - 2), (row + 1, column + 2), (row + 2, column - 1), (row + 2, column + 1))

      case _ =>
        List()
    }
  }.filter { case (n, m) =>
    (n >= 0) && (n < game.board.rows) && (m >= 0) && (m < game.board.columns)
  }

  /**
    * Returns boshops movement table
    * @param row      row number
    * @param column   column number
    * @param game     game object
    * @return table with all movement options
    */
  private def bishopMovement(row: Int, column: Int, game: Game) = {
    val rightStart = if(row < column)
      (0, column - row)
    else
      (row - column, 0)

    val leftStart = if(row + column > game.board.columns)
      (row + column - game.board.columns, game.board.columns)
    else
      (0, row + column)

    val rightTarget= if(game.board.rows < game.board.columns)
      game.board.rows
    else
      game.board.columns

    val leftTarget= if(row + column < game.board.rows)
      row + column
    else
      game.board.rows

    ((0 to rightTarget).toList.map { i =>
      (rightStart._1 + i, rightStart._2 + i)
    } ::: (0 to leftTarget).toList.map { i =>
      (leftStart._1 + i, leftStart._2 - i)
    }).filterNot { case (n, m) =>
      (n == row) && (m == column)
    }
  }

  /**
    * Returns rooks movement table
    * @param row      row number
    * @param column   column number
    * @param game     game object
    * @return table with all movement options
    */
  private def rookMovement(row: Int, column: Int, game: Game) = {
    ((0 to game.board.rows).toList.map { n =>
      (n, column)
    } ::: (0 to game.board.columns).toList.map { m =>
      (row, m)
    }).filterNot { case (n, m) =>
      (n == row) && (m == column)
    }
  }
}

object GamePiece {

  /**
    * Initializes number of game pieces of certain type
    * @param pieceType type of game piece
    * @return list of game pieces
    */
  def init(pieceType: GamePieceType): List[GamePiece] = {
    (1 to readInt(s"Enter number of ${pieceType.toString.toLowerCase}s: ", min = 0)).map { i: Int =>
      GamePiece(pieceType)
    }.toList
  }
}