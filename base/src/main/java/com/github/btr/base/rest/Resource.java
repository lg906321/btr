package com.github.btr.base.rest;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 资源
 * Created by ryze on 2017/5/1.
 */
public class Resource
{
	public final List<Link> links = new ArrayList<>();

	public void addLink(final String rel, final String href)
	{
		val data = Link.builder().href(href).rel(rel).build();
		links.add(data);
	}

	public void addLink(final Link data)
	{
		links.add(data);
	}

	public Optional<Link> getLink(final String rel)
	{
		return links.stream().filter(data -> data.getRel().equals(rel)).findAny();
	}

}
