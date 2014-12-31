package net.planetgeeks.mcstarter.event;

import lombok.Getter;
import lombok.Setter;

public class EventCancellable extends Event
{
	@Getter @Setter
    public boolean cancelled;
}
