         03  LZAPLHDR-AREA.
           05  LZAPLHDR-DCI-INF.
             07  LZAPLHDR-MENUID          PIC  X(08).
             07  LZAPLHDR-PGMID           PIC  X(08).
             07  LZAPLHDR-CONVSIGN        PIC  X(01).
             07  LZAPLHDR-DCI-RESERVE     PIC  X(23).
           05  LZAPLHDR-APPL-INF.
             07  LZAPLHDR-RTNCD           PIC  X(02).
             07  LZAPLHDR-RSNCD           PIC  X(04).
             07  LZAPLHDR-OTHCD           PIC  X(08).
             07  LZAPLHDR-MSGID           PIC  X(07).
             07  LZAPLHDR-MSGKBN          PIC  X(01).
             07  LZAPLHDR-MSG             PIC  G(50) USAGE DISPLAY-1.
             07  LZAPLHDR-USERDATA-LEN    PIC  9(08).
             07  LZAPLHDR-APPL-RESERVE    PIC  X(30).
