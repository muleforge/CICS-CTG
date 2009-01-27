      *     コマンド伝文
         03 SAMPLE-COMMAND.
      *       検索区分コード
      *       番号検索情報
           05 CUSTOMER-INFO.
      *         お客さま番号
             07 CUSTOMER-NO                    PIC X(10).
      *         顧客名
             07 CUSTOMER-NAME                  PIC G(20).
      *         顧客住所
             07 CUSTOMER-ADDRESS               PIC G(20).
      *         特定対象年月日（検索用）
             07 SEARCH-DATE                    PIC X(8).

