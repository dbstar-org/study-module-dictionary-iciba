package io.github.dbstarll.study.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.info.Describable;
import io.github.dbstarll.dubai.model.entity.info.Namable;
import io.github.dbstarll.study.entity.enums.ExchangeKey;
import io.github.dbstarll.study.entity.join.BookBase;
import io.github.dbstarll.study.entity.join.WordBase;

import java.util.Date;

@Table
public interface Exercise extends StudyEntities, WordBase, BookBase, Namable, Describable {
  enum ExerciseKey {
    listen(3), spell(14), read(0), write(0);

    private final int maxLevel;

    private ExerciseKey(int maxLevel) {
      this.maxLevel = maxLevel;
    }

    public int maxLevel() {
      return maxLevel;
    }
  }

  ExerciseKey getExerciseKey();

  void setExerciseKey(ExerciseKey exerciseKey);

  ExchangeKey getExchangeKey();

  void setExchangeKey(ExchangeKey exchangeKey);

  int getLevel();

  void setLevel(int level);

  boolean isCorrect();

  void setCorrect(boolean correct);

  int getBingo();

  void setBingo(int bingo);

  Date getLast();

  void setLast(Date last);
}
