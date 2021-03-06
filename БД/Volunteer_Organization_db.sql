-- Adminer 4.8.1 PostgreSQL 14.3 dump

\connect "Volunteer_Organization_db";

DROP TABLE IF EXISTS "candidates";
CREATE TABLE "public"."candidates" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "activation" character varying(255),
    "city" character varying(255),
    "description" character varying(255),
    "email" character varying(255),
    "name" character varying(255),
    "phone" character varying(255),
    "photo" character varying(255),
    "status" character varying(255),
    CONSTRAINT "candidates_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "candidates" ("id", "activation", "city", "description", "email", "name", "phone", "photo", "status") VALUES
(2,	'a4858d85-26e5-4b2b-9169-77daf677dfbe',	'???????????????????????? ??????????????, ??. ????????????????',	'???????? ?????????????????? ??????????!',	'shulga.katerina@gmail.com',	'???????????? ???????????????? ????????????????',	'380501682593',	'ac86fd8c-8c78-49d1-8574-35b4deadb323_27244afe-0867-4d4e-9c77-87b89dd64fcb_Katerina.jpg',	'waiting'),
(9,	'confirmed',	'??. ??????????????',	'?????? ???????????? ????????, ???????? ?????????????????? ????????????????????????',	'grebelnik.viktor@gmail.com',	'???????????????????? ???????????? ????????????????????',	'380973871256',	'39c3d038-4184-4aec-8b93-df059d495556_27244afe-0867-4d4e-9c77-87b89dd64fcb_Viktor.jpg',	'waiting'),
(10,	'aca218c1-e295-46fa-836e-c234f768444c',	'???????????????????????????????? ??????????????, ??. ??????''??????????',	'???????? 30 ??????????, ?????????? ???????????????????? ???? ?????????????????????????? ????????????????????',	'riznik.oleg@gmail.com',	'???????????? ????????',	'380603451672',	'68145e2d-43d6-4de9-b309-b84fd27f43c2_Oleg.jpg',	'waiting'),
(1,	'confirmed',	'??. ????????',	'?? ???????????????????? ?????????????????? ???????????? ???????? ????????????????????????',	'gerega.viktoria@gmail.com',	'???????????? ???????????????? ??????????????????????????',	'380951473914',	'65a3eca3-a981-40ed-a880-744908dda0cf_viktoria.jpg',	'waiting'),
(19,	'confirmed',	'??. ??????????????',	'????????????????????',	'vopifa2799@krunsea.com',	'???????? ????????',	'3809514524',	'4ca90c28-a9cc-4d3a-aa00-df13e60a4fb4_39c3d038-4184-4aec-8b93-df059d495556_27244afe-0867-4d4e-9c77-87b89dd64fcb_Viktor.jpg',	'accept');

DROP TABLE IF EXISTS "categories_of_posts";
CREATE TABLE "public"."categories_of_posts" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "category" character varying(255) NOT NULL,
    "description" character varying(255) NOT NULL,
    CONSTRAINT "categories_of_posts_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "categories_of_posts" ("id", "category", "description") VALUES
(1,	'????????????????????????',	'????????????????????????'),
(2,	'?????????????????? ????????????',	'?????????????????? ????????????'),
(8,	'???? ???? ?????????????',	'???? ???? ?????????????'),
(9,	'?????????? ??????????',	'?????????? ??????????');

DROP TABLE IF EXISTS "comments";
CREATE TABLE "public"."comments" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "comment" character varying(255),
    "date" character varying(255),
    "id_post" bigint,
    "id_user" bigint,
    CONSTRAINT "comments_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "posts";
CREATE TABLE "public"."posts" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "date" character varying(255) NOT NULL,
    "description" character varying(10000) NOT NULL,
    "image" character varying(255),
    "title" character varying(255) NOT NULL,
    "id_category" bigint,
    "id_user" bigint,
    CONSTRAINT "posts_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "posts" ("id", "date", "description", "image", "title", "id_category", "id_user") VALUES
(7,	'12:05, 19.05.2022',	'?????????????????????? ?????????? ???????????? ???????? ???????????????????????? ????????????????, ?? ?????????? ?????????????????? ???? ?????????? ???????????????????? ???? ?????????????????????????? ??????????????????????. ?????????????? ???????? ?????? (????????????) ?? ?????????????? ?? ???????????????????? ???????????????????????? ???????????? ?????????????? ?? ?????????????????????????? ???????????? ???? ???????????? ???????????????? ???????????????? ???? ?????????????????????? ???????????????????????? ??????????????????. ???? ???????????????????????????? ????????????-?????????????? ?????????????????? ?????????????????????? ?????? ???????????? ???????????????? ???????????????????????? ????????????????????, ?? ?????????? ???????????? ??? ???????????? ???????????????????????? ?? ???????????? ??????????.

???????????? ???????????????????? ???????????????????? ???????????????? ???????????????????????? ?? ?????????????? ?????????? ???????????????? ???????????????????????????? ??????????????????????. ???????????????????????? ?????????????????? ?? ?????????????? ?????? ???????????????????????????? ??????????????????????????, ?????????????????????? ???? ??????????????.
?????????? ?????????????????????????? ?????????????????? ??? ?????????????????????? ???????????????????? ???????????? ?? ????????????????????????, ?????? ?????????????????? ????????????????????????, ???? ?????????? ?????????????????????? ?? ?????????? ???????????? ??????????. ???? ?????????????????? ???????????????????? ?????????????????????? ???????????? ?????????????????????? ???????????? ?????????????????????????? ????????????????, ?? ?????????????????? ??? ?????????????????????? ???????????????????? ?????????? ?????????????? ???? ?????????????? ?????????????????????? ????????????????????.

?????????????? ????????, ?????? ???????????????????? ?????????????????????????? ?? ??????????????, ?????????????????? ?????? ?????????? ???????????????????????????? ?? ??????????????????????. ???????? ???????????????? ????????????, ???????????????????? ?????????????? ?? ?????????????????? ???? ???????????????? ???? ?? ?????????? ????????????????. ???? ???????? ?????????? ?? ???????????????????? ???????????????? ??????????????????, ???? ???????????????? ???????????????????? ?????? ?????????????? ????????????????????, ?? ?????????? ?????????????????? ?????????????????????? ????????????, ?????? ?????????????? ?????? ???? ??????????????, ???? ?????????? ????????????????, ??? ?????????????????? ?????????? ????????????????????, ???????????? ?????????? ???????????? ?? ??????????????.

?????????????????? ?????????????????????????? ?????????????????? ?????????? ???????????????? ?????????????? ?????????????? ?????????????????? ???????????????? ?????????? ?????? (????????????) ?? ??????????????, ?????????????????????? ?????????????????????????? ???????????? ???? ???????????????? SoftServe ???? ?????????????????? ???????????????????????? ???????????? ???? ???????????? ??????????????.

??????? ????????????, ???? ???????????????????????? ??????????????. ???????? ????????, ???? ?????????????????????? ???????????????? ????????????, ???? ???????????? ?? ?????????????????????????? ???????????????????? ???? ???????????? ?????????????????? ?????????????? ???????? ???????? ?? ???????????????? ????????????, ?? ?? ?????????? ?????????? ???????????????? ?????????????????????? ?????? ???????????????? ??????????. ?????????????????? ?????????????????????????? ?????????????????? ??? ?????????? ???????????? ???????????? ???????????????????? ?? ???????????? ???????????????? ?????????? ???????????? ???? ???????????????????? ?????????????????????? ???????????????? ?????? ???????????????????? ???????????????? ??????????????????, ??? ???????????????? ?????????????? ???????????? ???? ???????????? ?????????? ??????????????.

???????? ?????????????????????? ??????, ?????? ?? ??????????????????, ?? ???????????????????????? ?????????????????????? ?? ???????????????? ???????????? ?????????????? ???? ?????????????????????? ???????????????????????? ???????????????????? ???? ???????????????????????? ??????????????????????: ?????? ???????????????? ???????????????? ???? ????????????-????????????????????????.

???? SoftServe, ???? ???????????????? ?????????????????????? ???? ???????????????????????????? ?? ????????????????????????????????, ???? ?????????? ???????????????????????? ???????? ????????????????????????. ????????, ??????????????????? ???????????????? ????????????, ???????????????????? ?? ??????????, ???????????? ?????????????? ????????. ???????? ???????????????? ?????????????? ???????? ?????????????? ???????? ?? ?????????? ?????? ?????? ???????? ?????????????????????? ??????????????. ?????????? ?? ?????????????????? ?????????????????????????? ???????????????????? ???????????????????????????????? SoftServe ?? ?????????????????? ???????????????? ?????????? ???????????????????? ?????????????????? ????????????????, ?????? ???? ???????????????? ???? ???? ?????? ???????? ??????????????, ?????? ?? ?????????????? ???????????????????? SoftServe??, ??? ???????????????????? ?????????????????????????? ???? ???????????? ???????? ???????????????????? SoftServe ?????????????? ????????????????.

???????????????? ???????? ???????????????????????? ?????????? ?????? ?????????? ??? ?????????? ?????????????????????????? ?????????????????????? ???? ???????????????????? ?????????????? ???? ?????????? https://platforma.volunteer.country/',	'508fabfb-fea5-4605-ba54-05615ca5c6ef_??????????????????????_2022-05-19_120348.png',	'?????????????????????? ???????????????????????? ??????????????????',	1,	3),
(22,	'14:39, 23.05.2022',	'?????????????????? ???????????? ???? ???????????????? ?????? ?????????????????????????????????? ??? ???????????? ?????????? ?? ?????????????????????? ?????????????? ???? ?????????????????????????? ????????????????. ?????????? ?? ??????, ???????? ???????????????????? ?????????? ?????????????? ?? ??????. ????, ???? ???? ??????????, ???????????? ????????????????????????.

???? ?????????? ?????????????????????? ?????????? ???????????????? ???????? ?????? ??????????????????? ?????? ?????????????????? ?????????? ???????????????? ?????? ???????????????????? ????????????? ???? ?????????????????????? ???????????????????????? ?????????????? ???? ???????????? ???? ????????????????????? ???? ?????????? ?????????????????????? ???????????????????????????

???????????????????????? ???????????????????? ??? ??????????????????????, ?????????????????? ????????????????????, ???????????????????????? ????????????????????, ???? ???????????????????????? ?????????????????????? ???????????? ?????????????? ?????????????????????????? ????????????????.

	?????????? ???? ????????''???????? ?????????????????? ?????????????????? ?? ???????????? 7 ???????????? ?????????????? ???????? ???????????????????????? ??????????????????????. ??????????????, ???????????????? ?????? ?????????? ????:
?????????????? ?????????? ???????????????????? ?????????????????????????? ????????????????????, ??????????????, ?????????????????? ??????????????????????, ???????????? ???? ???????????? ???????????????????? ?????? ?????????????? ???? ?????????? ?????????????????????? ?????????????????????????? ????????????????????, ???????????????????????? ???????????????????????? ???????????????? ??????????????, ???????????????????????? ???? ??????????????????????.
?????????????????????? ???????? ???????????????????? ?????????????????????????? ???????????????????? ???? ??????????????????-???????????????????? ???????????????? ?? ???????? ???? ?????????????????????? ???? ????????????????, ???? ???????????????????? ?????????????????????? ??????????????????????????, ???? ???????????? ?????????????????????? ??????????????.
?????????????????????????? ????????????, ??????''???????????? ???? ?????????????????????? ?????????????????????????? ????????????????????, ???????????????????????? ?????????????? 11 ?????????? ????????????;
???????? ??????????, ?????????????????????? ?????????????????? ?????? ?????????????????????? ?????????????????????????? ???????????????????? ???? ????????????????????????????.

	???????????? ????????????, ?????????? ?????????? ?????? 14 ???? 18 ?????????? ???????????? ?????????????????????? ???????????????????????? ???????????????????? ???? ???????????? ??????????????, ?????????????????? ??????????????, ??????????????-?????????????????????? ?????? ??????????????????????????.

???????? ???? ???????????? ???????????????? ???????????????? ?? ???????????????? ????????????????, ?????? ?????? ???????????????????? ?????????????????? ???????????????????????? ???????????????? ???????????????????????? ?????? ???????????????????? ??????????????????, ?? ?????????? ???????????????? ???????????????????????? ???????????????? ???????????????? ?????????? ?????????????? ???? ?????????? ???????????????????? ??????????????????????.

?????? ????????, ?????? ?????????? ???????????? ?? ?????????????????? ?????????????????????????? ????????????????, ???? ???? ??????????''?????????? ?????????????????????? ??????????????. ?????????????????? ???????????????????? ???? ??????????????????????, ?????? ??????????????????????, ???? ?????????????????????????? ???????? ????????????????.

?????????? ?? ??????, ?????????????????? ?????????????????????? ?????????????????????? ???????????????????? ?????????????????? ??????????????. ???????? ???????? ?????????????? ???????????????? ???????????????????????? ?????????????? ???? ???????????????????????? ?????? ?????????????????? ??????????????.

???????? ???? ????????????????????, ???? ???????????? ?????????????????????? ???????? ????????''???????? ?????? ????????''???????? ??????????????????????, ?????????????????????? ???? ???????????????????? ???????????????? ?????? ???????????????????????? ????????????????????. ???? ?????????? ???????? ??????????. ??????????????????, ?????????? ?????????????????? ?????????????? ?? ????????????????????????, ???????? ???? ???????????????? ?????????????????????? ?? ?????? ???? ?????????????????? ????????????.',	'64731e11-ee73-45fe-8cea-189f6e7ab5fb_??????????????????????_2022-05-23_143909.png',	'?????????? ???? ????????''???????? ????????????????????',	1,	1),
(24,	'14:48, 23.05.2022',	'???????????? ?????????????????? ???????????? ?????????????? ???????????? ????????????????????????, ???????? ???????????????????????????? ???? ?????????????????? ???????????????????? ?????? ?? ??????????????. ?? ???????????? ?????????????? 75 ?????????? ?? ???????????? ???????????????? ???????????? ?????????????? ?????????????????? ???????????????? ?? ??????????, ???????????? ???? ??????????????. ?????????? ???????????????? ???????????????? ?????????????????????? ?? ?????????? ??????????, ???? ???? ???????????? ???????????? ????????????????: ???????????????? ?????????????? ?????? ???????????????? ?????????? ???????????????????????? ?????? ?????????? ????????????????.

???????????????????????? ?????????? ???????????????? ?????? ???????????? ??????????????. ???????? ???? ??????, ???? ???????????????? ?????????????? ????????. ?????????????? ???????????????????????? ?????????? ???????????? ?????????????????? ?????????????????? ?????????????? ??????????????????????, ?????????????????????????? ?? ?????????????????????? ???? ???????????? ???????? ????????????????????, ?????????????????? ???????????? ?????????????? ???? ?????????????? ???????????????????? ??????????????????.


???? ??????????, ???????????????????????? ?????????????????? ?????????????????????????? ???????? ???? ?????????????? ???????? ???????????????? ??????????????????, ???????? ?????????????? ???????? ??? ?? ?????????????? ???????????????????? ?????? ????????, ?????? ?????????????????? ?????????????????? ?? ??????????????.

?? ?? ?????????? ????????, ???????????????????????? ?????????????????? ???????????? ???????? ??????????. ?? ?????????? ?????? ???????????????????? ???????????????????? ???????? ?? ?????????????????? ???????????????? ???? ?????????????? ???????????????? ?????????? ???????????????????? ??????????.

???????????? ?????????????????? ???????????????? ?????????? ?? ?????????????????? ???????? ???? ??????????. ?????????? ???????????????????? ?? ???????????????? ?????????????? ?????????? ?????????????????? ?????????????????? ?????? ????????????????????, ???????? ???????? ?? ?????????????? ???????????????????? ???? ???????????? ???? ?????????????????????? ?????? ???????? ???????????? ????????????.

???? ????????????????, ?? ?????????????????? ???????? ???????????????? ?????????? ?????????????? ?????????????? 40 ???????????? ?????? ????????????????????????. ?????????????? ????????????????: ???????????? ?????????????? ???? ???????????? ?????????????? ?????????????????????? ?? ?????????????????? ???? ?????????????????????? ?????????? ?????????? ?????????????? ??????????. ?????? ?????? ???????????????????????? ??? ???? ?? ?????????? ?????????? ???????? ???????????? ??????????????????????. ?????????? ???????????? ?????????????? ???????????????????????????? ?? ???????? ????????????????????: ?????????????? ???????????????????????? ??????????, ???????????????????????? ???????? ???????????? ???? ?????????? ?????????????????????? ???? ???????????????????????? ?????????????? ?? ??????????.

?????????? ?????????????????? ?????????? ?????????????????? ?? ????????????, ????????????????????, ????????????????????????? ???? ???????????????? ?????????? ?????????????????? ????????????. ?????? ???????????????? ?????????????? ?????????????????????? ?? ???????????????? ??????????????, ???????? ???????????? ?????????????????? ?????? ???????????????????????? ?????????????? ???? ???????????? ????????????.

?????? ???????????????? ?????? ?????????????????????? ???????? ???????????????????? ?????????????????????? ???????????? ?????????????????????? ???? ??????????????. ?????????????? ???????????????? ???? ???????????????????? ????????????, Veteran Hub, ?????????????????????????????? ?????????????????????? ????????????, ?????????????????????????? ?? ???????????????????????????? ?????????????????? ?????????? ?????????????????? ???? ???????? ?????????????? ?? ?????????????????????? ???????????????? ???????????? ???? ???????????? ??????????????, ?????????????? ??????????????????.

???????????? ??????????????????????????? ?????????????? ?? ?????????????????? ???????????????????? ???????????????? ?????????????????????? ?????? ?? ??????????????, ???? ???????? ???????????????? ???????????????????? ?????????????????? ?????? ?????????????? ???????????????????? ?? ?????? ?????? ????????????.',	'5dcd5bdc-feff-4713-b7c4-e9c5653bd7db_??????????????????????_2022-05-23_144855.png',	'?????? ?????????????? ?????????? ????????????????????????, ???????????? ???? ????????????????????  ???????????????? ?????????????? ????????????????????????',	2,	1),
(23,	'14:43, 23.05.2022',	'?????????????? ?????????????????????????? ??? ???? ?????????? ???????????????? ???????????????? ?????????????????????????? ???????????????? ???? ???????? ??????????????. ???????????????? ???????? ?????????????? ?????????????????? 42 ???????????????? ?? ???????? ???????????????? ????????????, ?????? ???????? ?????????????????? ???? ???????????????????? ?????????????????????????? ?????? ?? ???????????? ??????????.

21-23 ?????????? ???? ???????????????????? ???? ???????????? ????????????????????????, ???? ?????????????? ?????? ?????? ????????????, ????????????????????????, ?????????????????????????? ?????????????????????? ???? ???????????? ????????????.

?????????? ???????? ???????????????????? ???? ???????????????????? ?? ???????????????????? ???? ???????????????????????? ????????????????, ?????? ???????????????????? ???? ???????????? ????????????????????????, ?? ?????????? ?????????????????? ???? ???????????????? ?????????????????????? ?????????????????????????? ????????????. ???????? ???? ???????? ???????????????????????????????? ?????????????????????????? ???????????????? ???????? ???? ???????????????????????????? ?????????????????? ?????????????????? ?? ?????????? ????????????.

???????????????????? ?????????? ?????????????????????? ???????? ???????????????????? ?????????????????? ?????????????????? ???????????? ?????? ???????? ???????????????????????? ???? ???????????????? ?????????????????????? ?????????????????????????? ????????????. ?????????? ???????????????????????? ???????????????????? ?????????????????? ???????????? ?????? ???????????????? ????????.

???????? ?????????????????? ???? ??????????, ???? ?? ?????????????? ???? ????????????????????: ??It???s always seems impossible until it???s done?? ???? ???????????????? ?????????????????? ???????????????? ?? ???????????? ???????? ???? ???????????? ?????????????? ?????????? ?? ?????????? ????????????.

???????????? ??????????????????????, ??????????-????????????????????, ?????????????? ?????????????? ???? ???????????? ???????? ???? ??????. ???????????? ???????? ?????? ???????????????????? ???? ???????????????? ???????????? ???? ???????????? ?????????????? ?? ????????????.

???????????? ????????. ???????? ?????? ?????????????????????? ???????????????? ?????????????????????????? ????????????????, ?????????????? ?????? ???????????????? ?????????????????? ???????????? ?????????????????????????? ???????????????????? ???? ???????????????? ?? ?????????????? ???????????????????????????? ???????? ?????????? ???? ???????????????????? ?????????????????????? ?????????????????? ???????????????? ???? ?????????????????? ?????????????????????? ???????????????????? ??????????????????.

???????????????????????? ???????????????????? ?????????????????????????? ?? ?????????????????? ???????????????????? ???????????????? ?????? ????????, ?????????????????? ?????????? ???????? ??????????????????, ???? ?? ???????? ???????????????????????? ???????????????????? ?? ???????? ???????? ???????? ????????.

???????? ????????, ???????????? ?????????????? ?????? ???? ???????????????????? ?????????????? ?????????????????? ???? ?????????? ????????????????????????, ???????????????? ?????????????????? ?????????????????????? ?????? ????????????????????????, ?? ?????????? ???????????? ?????????????????????????? ????????????????????????.

???????????? ????????. ???????????? ???????? ???????????? ???????????????????????? ???? ?????????????????? ???? ?????????????? ?????? ????, ???? ???????????????? ???????????????????? ???????? ?????????????? ???? ?????????????? ???? ?????????????????? ???????????????? ???? ???????????????????????? ??????????????.

?????? ???????????????????? ?????? ??????????, ?????? ?????????? ?????????????? ?????????????? ???????????????? ???? ?????????? ?? ????????????.

?????? ?????????????????????? ?????????? ?????????????? ?????????? ???????????? ???????????????? ?????????????????????????? ?????? ?????????????? ???????????????????????? ???????????????????????????? ?????????????????? ????????????????????.

?????? ???????????????????? ???????????? ???????????????????? ???? ?????????????? ?????????? ?????????????????? ??????????.

?????????? ???? ?????????????????? ???????????????? ???? ?????????????????? ???? ???????????? ?????????????????????????? ?????????????? ?????????????? ???????? ???????????????? ?????? ???????????????? ?????????????????????????? ?????? ?? ??????????????, ????????????????????????? ???? ?????????? ???? ???? ??????????????????.

???????????????? ???????????????? ?????????????????? ???????????????????? ???????????????????? ???? ???????????????? ???????????????? ?????????????????? ???????????? ??????????????? ???????????? ???? ???????????????????? ???????? ?? ???????????????????????????? ????????????????????????.

?????????????????? ?????????? ???????????????????????? ?????????????????????? ?????????????????? ?????????? ???????????????? ???????????????????????? ?? ????????????????. ???????????????? ???????????????? ???????????? ?????????????????? ?????? ?????????????????????????????? ?????????????? ???????????????? ???????????????????????? ?? ?????????? ????????????????????. ?????????????? ???????????????? ?????????? ?????? ?????? ???? ???????????????? ???????? ?????????? ???????????????????? ???? ??????????????.

???????????????? ?????????????? ???????????????? ???? ???????????????? ?? ????????????, ?????????? ???? ?????????? ???? ?????????? ??????????????, ?????? ????????, ?????? ?????????????????? ???????????????????????? ?? ?????????? ???????????????? ???? ???????????????????? ???????????????? ?????????? 2 ????????????.',	'616e37d7-c20b-4f26-b7cb-df26dc33ebd5_??????????????????????_2022-05-23_144321.png',	'?????????? ????????????????????????: ???? ?????????????? ???????????????? ?????????? ?????????????? ????????????????????????,?????????? ????????????????????????: ???? ?????????????? ???????????????? ?????????? ?????????????? ????????????????????????',	2,	1);

DROP TABLE IF EXISTS "project_info";
CREATE TABLE "public"."project_info" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "email" character varying(255) NOT NULL,
    "image" character varying(255) NOT NULL,
    "logo" character varying(255) NOT NULL,
    "name" character varying(255) NOT NULL,
    "phone" character varying(255) NOT NULL,
    "telegram" character varying(255) NOT NULL,
    CONSTRAINT "project_info_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "project_info" ("id", "email", "image", "logo", "name", "phone", "telegram") VALUES
(1,	'volunteerOrg@gmail.com',	'logo_for_navbar.jpg',	'logo.ico',	'Org',	'380123456789',	'https://t.me/V_Zelenskiy_official');

DROP TABLE IF EXISTS "roles";
CREATE TABLE "public"."roles" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "description" character varying(255),
    "role" character varying(255),
    CONSTRAINT "roles_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "roles" ("id", "description", "role") VALUES
(1,	'admin',	'admin'),
(2,	'user',	'user'),
(3,	'editor',	'editor');

DROP TABLE IF EXISTS "suggested_posts";
CREATE TABLE "public"."suggested_posts" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "date" character varying(255) NOT NULL,
    "description" character varying(10000) NOT NULL,
    "image" character varying(255),
    "status" character varying(255) NOT NULL,
    "title" character varying(255) NOT NULL,
    "id_category" bigint,
    "id_user" bigint,
    CONSTRAINT "suggested_posts_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "suggested_posts" ("id", "date", "description", "image", "status", "title", "id_category", "id_user") VALUES
(1,	'11:02, 26.05.2022',	'test',	'27244afe-0867-4d4e-9c77-87b89dd64fcb_??????????????????????_2022-05-26_110214.png',	'waiting',	'test',	1,	2);

DROP TABLE IF EXISTS "users";
CREATE TABLE "public"."users" (
    "id" bigint DEFAULT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "blocked" boolean,
    "email" character varying(255),
    "name" character varying(255),
    "password" character varying(255),
    "id_candidate" bigint,
    "id_roles" bigint,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OpenServer\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "users" ("id", "blocked", "email", "name", "password", "id_candidate", "id_roles") VALUES
(1,	'0',	'adm@com',	'admin',	'$2a$12$1G8VrJOIK.cWGJ13.NnrNeQiBx0x2tJc8k6Puct1otwRi/Nl3qRbm',	NULL,	1),
(3,	'0',	'ed@gmail.com',	'editor',	'$2a$12$ag3Z4dvBfRKzmb1L9l0TgeiRjonz8jf1Hw1oSlNxHcqbiLC8nHGeS',	NULL,	3),
(2,	'0',	'ddd@gmail.com',	'user',	'$2a$12$ag3Z4dvBfRKzmb1L9l0TgeiRjonz8jf1Hw1oSlNxHcqbiLC8nHGeS',	NULL,	2),
(4,	'0',	'user@gmail.com',	'user',	'$2a$12$JZJqtmODD3N6noNyg57EBua3FpmUUbTumTIOdO/edrlyZ634dtWBm',	NULL,	2);

ALTER TABLE ONLY "public"."comments" ADD CONSTRAINT "fk2e1j871ildbkagpidsson8krk" FOREIGN KEY (id_user) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."comments" ADD CONSTRAINT "fk9w3fyg8dv7lsosmxiky6p6x7n" FOREIGN KEY (id_post) REFERENCES posts(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."posts" ADD CONSTRAINT "fk9m6je90vcy42ymfro5e2haryb" FOREIGN KEY (id_category) REFERENCES categories_of_posts(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."posts" ADD CONSTRAINT "fki39p5vj96ak6nrx3k8kswyr8k" FOREIGN KEY (id_user) REFERENCES users(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."suggested_posts" ADD CONSTRAINT "fk75gc5d8iuvj4vapgujpjn3hh9" FOREIGN KEY (id_user) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."suggested_posts" ADD CONSTRAINT "fk7pe0ih69qpf007yaqmf2weiq6" FOREIGN KEY (id_category) REFERENCES categories_of_posts(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."users" ADD CONSTRAINT "fk39yw01qp6l45ng6p0k71ny4s1" FOREIGN KEY (id_candidate) REFERENCES candidates(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."users" ADD CONSTRAINT "fk5i1go2t0eqag1spg3ubr63amq" FOREIGN KEY (id_roles) REFERENCES roles(id) NOT DEFERRABLE;

-- 2022-06-15 08:37:31.552082+03
