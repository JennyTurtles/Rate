ALTER TABLE paper
MODIFY name VARCHAR(100);

UPDATE menu SET enabled = 0 WHERE path = '/student/Paper';
UPDATE menu SET enabled = 0 WHERE path = '/student/Patent';
UPDATE menu SET enabled = 0 WHERE path = '/student/ResearchAward';
UPDATE menu SET enabled = 0 WHERE path = '/student/HorizontalResearchProject';
UPDATE menu SET enabled = 0 WHERE path = '/student/ResearchProject';
UPDATE menu SET enabled = 0 WHERE path = '/student/AcademicMonograph';
UPDATE menu SET enabled = 0 WHERE path = '/student/AcademicCompetition';
UPDATE menu SET enabled = 0 WHERE path = '/student/Decision';
UPDATE menu SET enabled = 0 WHERE path = '/student/Product';
UPDATE menu SET enabled = 0 WHERE path = '/student/Standard';
UPDATE menu SET parentId = 94 WHERE path = '/student/Achievements';
UPDATE menu SET url = '/salary/search/**' WHERE id = 70 ;
UPDATE menu SET path = '/student/Project' WHERE id = 70 ;
UPDATE menu SET component = 'Project' WHERE id = 70 ;
UPDATE menu SET parentId = 94 WHERE id = 70 ;
UPDATE menu SET enabled = 0 WHERE id = 69 ;
UPDATE menu SET name = '期刊添加管理' WHERE id = 107 ;
UPDATE menu SET name = '项目开发' WHERE id = 130 ;
UPDATE menu SET name = '工作量记录' WHERE id = 131 ;
INSERT INTO menu (id,url, path, component, name, requireAuth, parentId, enabled)
VALUES (137,'/salary/search/**', '/student/DeclareList', 'DeclareList', '成果申报', 1, 94, 1);

UPDATE menu_role SET mid = 70 WHERE id = 403 ;
UPDATE menu_role SET rid = 7 WHERE id = 403 ;

INSERT INTO menu_role (id,mid,rid)
VALUES (404,137,7);

CREATE TABLE `xin_project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '论文题目',
  `point` int DEFAULT NULL COMMENT '积分',
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `sname` varchar(255) DEFAULT NULL COMMENT '参与人',
  `state` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'commit' COMMENT '状态',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `mid` int DEFAULT NULL COMMENT '中间id',
  `type` varchar(255) DEFAULT NULL COMMENT '类别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;




ALTER TABLE programrecord MODIFY COLUMN ID INT AUTO_INCREMENT;





UPDATE menu
SET path = '/student/DeclareList',
    component = 'DeclareList',
    name = '成果申报'
WHERE id = 70;

UPDATE menu
SET path = '/student/Project',
    component = 'Project',
    name = '成果列表'
WHERE id = 137;

UPDATE menu_role SET mid = 137 WHERE id = 403;
UPDATE menu_role SET mid = 70 WHERE id = 404;





ALTER TABLE i_publication
MODIFY name VARCHAR(200);


UPDATE menu SET enabled = 0 WHERE id = 130;
UPDATE menu SET parentId = 43 WHERE id = 131;
UPDATE menu SET enabled = 0 WHERE id = 56;
UPDATE menu SET enabled = 0 WHERE id = 57;
UPDATE menu SET enabled = 0 WHERE id = 58;
UPDATE menu SET enabled = 0 WHERE id = 60;
UPDATE menu SET enabled = 0 WHERE id = 111;
UPDATE menu SET enabled = 0 WHERE id = 112;
UPDATE menu SET enabled = 0 WHERE id = 118;
UPDATE menu SET enabled = 0 WHERE id = 120;
UPDATE menu SET enabled = 0 WHERE id = 122;
UPDATE menu SET enabled = 0 WHERE id = 126;
UPDATE menu SET parentId = 43 WHERE id = 63;
UPDATE menu SET parentId = 43 WHERE id = 65;
UPDATE menu SET parentId = 43 WHERE id = 98;
UPDATE menu SET parentId = 43 WHERE id = 101;
UPDATE menu SET parentId = 43 WHERE id = 105;
UPDATE menu SET parentId = 107 WHERE id = 108;
UPDATE menu SET parentId = 43 WHERE id = 109;
UPDATE menu SET parentId = 43 WHERE id = 109;
UPDATE menu SET name = '指标点审核进度' WHERE id = 109;
UPDATE menu SET parentId = 43 WHERE id = 124;
UPDATE menu SET parentId = 43 WHERE id = 129;
UPDATE menu SET parentId = 43 WHERE id = 131;
UPDATE menu SET parentId = 43 WHERE id = 133;
UPDATE menu SET parentId = 43 WHERE id = 134;

DELETE FROM menu_role WHERE id IN (313, 315, 348, 352, 358, 368, 367, 392, 397, 399, 401);
DELETE FROM menu_role WHERE id IN (357,359,360,361,387,386,371,363,385,390,384);
UPDATE menu_role SET rid = 11 WHERE id IN (403, 404);

INSERT INTO menu (id,url, path, component, name, requireAuth, parentId, enabled)
VALUES (138,'/salary/search/**', '/teacher/Project', 'Project', '学生成果列表', 1, 43, 1);
INSERT INTO menu (id,url, path, component, name, requireAuth, parentId, enabled)
VALUES (139,'/salary/search/**', '/admin/Project', 'Project', '学生成果列表', 1, 43, 1);

INSERT INTO menu_role (mid,rid)
VALUES (138,9);
INSERT INTO menu_role (mid,rid)
VALUES (139,14);
INSERT INTO menu_role (mid,rid)
VALUES (108,11);






ALTER TABLE xin_project
ADD COLUMN sid INT

ALTER TABLE i_application
ADD COLUMN createtime DATETIME

ALTER TABLE i_award
ADD COLUMN createtime DATETIME

ALTER TABLE i_book
ADD COLUMN createtime DATETIME

ALTER TABLE i_competition
ADD COLUMN createtime DATETIME

ALTER TABLE i_decision
ADD COLUMN createtime DATETIME

ALTER TABLE i_patent
ADD COLUMN createtime DATETIME

ALTER TABLE i_project
ADD COLUMN createtime DATETIME

ALTER TABLE i_standard
ADD COLUMN createtime DATETIME

ALTER TABLE paper
ADD COLUMN createtime DATETIME

ALTER TABLE i_project
ADD COLUMN date DATETIME

ALTER TABLE i_project
ADD COLUMN have_score INT




ALTER TABLE i_competition
MODIFY date TIMESTAMP;

