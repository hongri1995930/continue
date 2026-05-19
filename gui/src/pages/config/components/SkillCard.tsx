import { BrowserSerializedSkill } from "core";
import { Card } from "../../../components/ui";

interface SkillCardProps {
  skill: BrowserSerializedSkill;
}

export function SkillCard({ skill }: SkillCardProps) {
  return (
    <Card className="flex flex-col gap-1.5 px-4 py-3">
      <span className="text-sm font-semibold">{skill.name}</span>
      <div className="text-xs">
        <span className="font-semibold">Description: </span>
        <span className="text-description italic">{skill.description}</span>
      </div>
      <div className="text-xs">
        <span className="font-semibold">Path: </span>
        <span className="text-description italic">{skill.path}</span>
      </div>
    </Card>
  );
}
