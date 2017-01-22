package com.mycompany.service;

import java.util.List;

import com.mycompany.domain.Workordermanage;

public interface WorkordermanageService {

	public List<Workordermanage> findListNotStart();

	public void start(String id);

	public void save(Workordermanage model);

	public Workordermanage findById(String workordermanageId);

	public void checkWorkordermange(String taskId, Integer check, String workordermanageId);

}
