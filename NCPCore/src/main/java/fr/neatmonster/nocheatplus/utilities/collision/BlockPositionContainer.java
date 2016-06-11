package fr.neatmonster.nocheatplus.utilities.collision;

import java.util.LinkedList;

import fr.neatmonster.nocheatplus.components.location.BlockPositionGet;
import fr.neatmonster.nocheatplus.components.location.IAddBlockPosition;
import fr.neatmonster.nocheatplus.components.location.IContainBlockPosition;
import fr.neatmonster.nocheatplus.components.location.IGetBlockPosition;

/**
 * A kept-simple container for block positions, meant to provide fast adding and
 * fast contains checks (and fast clear). Intended use is to add blocks first,
 * then query if blocks are contained with advancing coordinates like with
 * ray-tracing or axis-tracing. Currently adding blocks twice is not meant to
 * happen, and we assume blocks to be few.
 * 
 * @author asofold
 *
 */
public class BlockPositionContainer implements IAddBlockPosition, IContainBlockPosition {

    // TODO: Not sure where to put this.
    // TODO: Future use / interfacing could involve collecting cuboids from block positions (mining/activity/areas).

    // TODO: Consider switching to HashSet or CoordMap.
    private final LinkedList<BlockPositionGet> blocks = new LinkedList<BlockPositionGet>();

    private int minX, minY, minZ, maxX, maxY, maxZ;

    public BlockPositionContainer() {
        resetBoundaries();
    }

    private void resetBoundaries() {
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        minZ = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        maxY = Integer.MIN_VALUE;
        maxZ = Integer.MIN_VALUE;
    }

    private void fitBoundaries(final int x, final int y, final int z) {
        minX = Math.min(x, minX);
        minY = Math.min(y, minY);
        minZ = Math.min(z, minZ);
        maxX = Math.max(x, maxX);
        maxY = Math.max(y, maxY);
        maxZ = Math.max(z, maxZ);
    }

    @Override
    public void addBlockPosition(final int x, final int y, final int z) {
        fitBoundaries(x, y, z);
        blocks.add(new BlockPositionGet(x, y, z));
    }

    @Override
    public boolean containsBlockPosition(final int x, final int y, final int z) {
        if (x < minX || y < minY || z < minZ || x > maxX || y > maxY || z > maxZ) {
            return false;
        }
        for (final BlockPositionGet pos : blocks) {
            if (x == pos.getBlockX() && z == pos.getBlockZ() && y == pos.getBlockY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsBlockPosition(final IGetBlockPosition blockPosition) {
        return containsBlockPosition(blockPosition.getBlockX(), blockPosition.getBlockY(), blockPosition.getBlockZ());
    }

    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    public void clear() {
        if (!isEmpty()) {
            resetBoundaries();
            blocks.clear();
        }
    }

}
