package dsp.net.motion.utils;

/**
 * 
 * 
 * 头尾均指向有效数据
 * 
 * @author WangZ
 *
 */
public class ByteCircularQueue {
	private byte[] buffer;
	private int head, tail;
	private boolean isEmpty = true;

	public ByteCircularQueue(int capacity) {
		buffer = new byte[capacity];
	}

	public void add(byte data) {
		if (full()) {
			throw new IllegalArgumentException();
		}
		synchronized (this) {
			
			if (!isEmpty) {
				tail = (++tail) % capacity();
			} else {
				isEmpty = false;
			}
			buffer[tail] = data;
		}
	}

	public byte peek() {
		if (empty()) {
			throw new IllegalArgumentException();
		}
		return buffer[head];
	}

	public byte remove() {
		if (empty()) {
			throw new IllegalArgumentException();
		}

		synchronized (this) {
			if (head == tail) {
				isEmpty = true;
				return buffer[head];
			}
			
			return buffer[(head++)%capacity()];
		}
	}

	public boolean full() {
		return (tail + 1) % capacity() == head;
	}

	public boolean empty() {
		return isEmpty && tail == head;
	}

	public int capacity() {
		return buffer.length;
	}

	public int size() {
		if (empty()) {
			return 0;
		} else {
			int mSize = tail - head;
			return mSize > 0 ? mSize + 1 : capacity() + mSize + 1;
		}
	}

	public int addAll(byte[] data) {
		if (size() + data.length > capacity()) {
			throw new IllegalArgumentException("size = " + size() + " len = " + data.length);
		}

		for (byte b : data) {
			add(b);
		}

		return size();
	}

	public byte[] removeAll(byte lenght) {
		if (size() < lenght) {
			throw new IllegalArgumentException("size = " + size() + " len = " + lenght);
		}

		byte[] data = new byte[lenght];

		for (byte i = 0; i < lenght; i++) {
			data[i] = remove();
		}

		return data;
	}

	@Override
	public String toString() {
		StringBuilder sbBuilder = new StringBuilder("[");

		for (byte i = 0; i < size(); i++) {
			sbBuilder.append((int) (buffer[(head + i) % capacity()] & 0xff));
			sbBuilder.append(",");
		}
		sbBuilder.append("]");
		return sbBuilder.toString();
	}

}
