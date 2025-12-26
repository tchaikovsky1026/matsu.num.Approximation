/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * 関数近似のためのコンポーネントを定義するパッケージ.
 * 
 * <p>
 * 関数近似は, 被近似関数 (近似される関数), 近似誤差の評価基準,
 * 近似区間, 近似関数空間によって特徴づけられる. <br>
 * このパッケージでは, 被近似関数, 近似区間, 近似誤差の評価基準 (の一部) を提供する.
 * </p>
 * 
 * <p>
 * 最もシンプルなのは,
 * {@code double} 型で表現された数体に関する関数近似である. <br>
 * {@code double} 型に関しては, 次が用意されている. <br>
 * {@link matsu.num.approximation.DoubleApproxTarget}
 * は, 被近似関数とその定義域 (= 近似区間), 近似誤差を評価するためのスケール因子を定義している. <br>
 * {@link matsu.num.approximation.DoubleFiniteClosedInterval}
 * は区間を表現するクラスである.
 * </p>
 * 
 * <p>
 * {@code double} 型に適用できる近似関数をマシンイプシロンレベルで得るためには,
 * {@code double} 型数体による関数近似では丸め誤差の観点から不十分であり,
 * より高い精度の数体が必要になる. <br>
 * そこで, 独自の数体を定義できる,
 * {@link matsu.num.approximation.PseudoRealNumber}
 * を用意している. <br>
 * {@link matsu.num.approximation.PseudoRealNumber}
 * に関するコンポーネントは,
 * {@link matsu.num.approximation.ApproxTarget},
 * {@link matsu.num.approximation.FiniteClosedInterval}
 * である. <br>
 * {@link matsu.num.approximation.PseudoRealNumber}
 * の実装として
 * {@link matsu.num.approximation.Decimal128}
 * を用意しているが, ユーザーが独自実装を用意しても良い
 * (独自実装を用意する場合は,
 * {@link matsu.num.approximation.PseudoRealNumber}
 * の実装要件に遵守すること).
 * </p>
 * 
 * <p>
 * 近似結果は, {@link matsu.num.approximation.ApproxResult} にラップされて返される. <br>
 * {@link matsu.num.approximation.ApproxResult} は
 * {@link java.util.Optional} の模倣であり,
 * 失敗の場合は "空" と, メッセージが返されることになる.
 * </p>
 * 
 */
package matsu.num.approximation;
