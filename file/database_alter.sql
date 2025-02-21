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


