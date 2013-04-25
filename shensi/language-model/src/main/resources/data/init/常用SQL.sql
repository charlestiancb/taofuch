-- 查看计算出的tf/idf的所有信息。
select ti.document_title,i.word,ti.tf,i.idf,ti.tf_idf from
				 WORD_TF_IDF ti left join WORD_IDF i on i.rec_id = ti.word_id
				 order by ti.rec_id;