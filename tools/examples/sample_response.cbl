      *     レスポンス伝文
         03 SAMPLE-RESPONSE.
      *       全レコード件数
           05 NO-OF-RECORDS                    PIC 9(5).
      *         対象年月日
           05 SEARCH-DATE                      PIC X(8).
      *       一覧情報
           05 QZO19R-LIST-INF                  OCCURS 5.
      *         契約番号
             07 CUSTOMER-NO                    PIC X(10).
      *         顧客名
             07 CUSTOMER-NAME                  PIC G(20).
      *         顧客住所
             07 CUSTOMER-ADDRESS               PIC G(20).
      *         月使用量
             07 CUSTOMER-AMOUNT                PIC 9(10).
      *         月使用料金
             07 CUSTOMER-CHARGE                PIC 9(10).

