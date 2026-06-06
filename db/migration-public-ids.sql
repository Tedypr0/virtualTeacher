-- Backfill public_id for existing rows (run once after columns are added by Hibernate ddl-auto=update)
UPDATE users SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE courses SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE lectures SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE notes SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE homeworks SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE courses_comments SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE lectures_comments SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
UPDATE ratings SET public_id = UUID() WHERE public_id IS NULL OR public_id = '';
